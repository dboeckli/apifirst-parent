#!/usr/bin/env bash
# Regenerates the Bruno requests under openapi/apifirst from the OpenAPI spec
# that apifirst-api bundles from @dboeckli/apifirst-sb.
set -o errexit
set -o nounset
set -o pipefail

script_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
collection_dir=$(cd "$script_dir/.." && pwd)
repo_root=$(cd "$collection_dir/../.." && pwd)
spec=${1:-$repo_root/apifirst-api/api-def/dist/bundle.yaml}
target_dir=$collection_dir/openapi/apifirst

if [[ ! -f $spec ]]; then
	echo "OpenAPI spec not found: $spec" >&2
	echo "Run './mvnw -pl apifirst-api generate-sources' first." >&2
	exit 1
fi

if ! command -v bru >/dev/null 2>&1; then
	echo "Bruno CLI (bru) is required: npm i -g @usebruno/cli@4.2.1" >&2
	exit 1
fi

tmp_dir=$(mktemp -d)
trap 'rm -rf "$tmp_dir"' EXIT

bru import openapi \
	--source "$spec" \
	--output "$tmp_dir/import" \
	--collection-name apifirst \
	--collection-format opencollection \
	--group-by path

rm -rf "$target_dir"
mkdir -p "$target_dir"
shopt -s nullglob
for entry in "$tmp_dir/import"/*; do
	name=$(basename "$entry")
	if [[ -d $entry && $name != environments ]]; then
		cp -r "$entry" "$target_dir/"
	fi
done

# Imported requests use {{baseUrl}}; the collection environments provide application-port.
grep -rl '{{baseUrl}}' "$target_dir" | while IFS= read -r file; do
	sed -i 's|{{baseUrl}}|http://localhost:{{application-port}}|g' "$file"
done
