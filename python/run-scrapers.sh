#!/bin/bash

cd python
python -m venv env
source env/bin/activate
pip3 install -q -r requirements.txt
python3 espn-scraper.py $1 $2 $3 $4 $5
echo "Ran espn-scraper.py"
python3 hashtag-scraper-stats.py $1 $2 $3 $4 $5
echo "Ran hashtag-scraper-stats.py"
python3 hashtag-scraper-zscores.py $1 $2 $3 $4 $5
echo "Ran hashtag-scraper-zscores.py"
deactivate
cd ..
