#!/bin/bash
cat package.json | jq -r .version | perl -ple '$"=".";@v=split/\./;$v[@v-1]++if@v;$_="@v";'
