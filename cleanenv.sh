#!/usr/bin/env bash
FILES_TO_KEEP=".git .idea"

# Try to stash changes
git stash push -m "tmp_clean"

# Exit if stash failed (in case this is the wrong folder
if [[ $? != 0 ]]
then
  echo "Error: Couldn't stash"
  exit 1
fi

# backup files and folders we want to keep (.git for example)
 for file in ${FILES_TO_KEEP}
 do
    mv "$file" "../tmp_clean_script-$file"
 done

# Delete all files from folder
rm -rf *
rm -rf .[a-zA-Z_]* # delete hidden folders

# restore files we wanted to keep
for file in ${FILES_TO_KEEP}
 do
    mv "../tmp_clean_script-$file" "$file"
 done

# reset git to last commit
git reset --hard

# restore stash, making sure that there is one and only one tmp_clean stash
stash_list=$(git stash list | grep "tmp_clean" | cut -d: -f1)
tmp_clean_stash_list_length=${#stash_list}

if [[ ${tmp_clean_stash_list_length} == 9 ]]
then
    git stash pop $(git stash list | grep "tmp_clean" | cut -d: -f1)
else
    echo "WARNINIG: tmp_clean not found or multiple stashes found"
fi




