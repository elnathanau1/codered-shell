#!/bin/bash

cd python
python3 -m venv env
source env/bin/activate
pip3 install -q -r requirements.txt
python3 espn-scraper.py localhost 5432 codered_local postgres postgres
echo "Ran espn-scraper.py"
python3 hashtag-scraper.py localhost 5432 codered_local postgres postgres
echo "Ran hashtag-scraper.py"
deactivate
cd ..
