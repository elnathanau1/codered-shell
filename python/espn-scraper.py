from bs4 import BeautifulSoup
import requests
import psycopg2
import sys
import logging
import datetime

ESPN_API_URL = "https://fantasy.espn.com/apis/v3/games/fba/seasons/2020/segments/0/leaguedefaults/1?view=kona_player_info"

def connect_to_db(host, port, database, user, password):
    conn = None
    try:
        conn = psycopg2.connect(host=host, port=port, database=database, user=user, password=password)
        cur = conn.cursor()

        # print('PostgreSQL database version:')
        # cur.execute('SELECT version()')
        #
        # # display the PostgreSQL database server version
        # db_version = cur.fetchone()
        # print(db_version)

        # # close the communication with the PostgreSQL
        #  cur.close()
        return conn
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)

def pull_data():
    # grab the page - note verify=False is not safe
    page = requests.get(ESPN_API_URL, verify=False)
    json = page.json()

    player_list = []
    for i in range(0, len(json['players'])):
        try:
            name = json['players'][i]['player']["fullName"].replace(".", "")
            rank = json['players'][i]['player']["draftRanksByRankType"]["STANDARD"]["rank"]
            adp = json['players'][i]['player']["ownership"]["averageDraftPosition"]

            formatted_data = [rank, name, adp, datetime.datetime.now()]
            player_list.append(tuple(formatted_data))
        except:
            pass

    return player_list

def push_to_db(player_list, conn):
    insert_sql = """INSERT INTO public.espn_rankings (rank, name, adp, last_modified) VALUES(%s,%s,%s,%s);"""
    delete_sql = """DELETE FROM public.espn_rankings"""
    cur = conn.cursor()
    cur.execute(delete_sql)
    cur.executemany(insert_sql, player_list)
    conn.commit()
    cur.close()

if __name__ == '__main__':
    if len(sys.argv) != 6:
        print("Incorrect number of parameters: [host port database user password]")
        sys.exit(0)

    conn = connect_to_db(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5])
    logging.info('Opened database connection')
    player_list = pull_data()
    logging.info('Scraped data from hashtagbasketball.com')
    push_to_db(player_list, conn)
    logging.info('Updated data in database')
    conn.close()
    logging.info('Closed database connection')
