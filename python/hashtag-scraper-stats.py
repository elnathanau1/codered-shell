from bs4 import BeautifulSoup
import requests
import psycopg2
import sys
import logging
import datetime

HASHTAG_RANKINGS_URL = "https://hashtagbasketball.com/fantasy-basketball-rankings"

# http://www.postgresqltutorial.com/postgresql-python/connect/
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
    page = requests.get(HASHTAG_RANKINGS_URL, verify=False)

    soup = BeautifulSoup(page.content, 'html.parser')
    players_div = soup.findAll("div", {"class" : "table-responsive"})[2]
    rankings_table = players_div.findAll("tr")

    player_list = []
    for i in range(0, len(rankings_table)):
        row = rankings_table[i]
        # print(row.prettify())
        td = row.findAll("td")
        if len(td) == 0:
            continue
        else:
            rank = td[0].string.strip()
            if rank != "R#":
                # unspan the tags that have it (not all of them do)
                formatted_data = [""] * len(td)
                for j in range(0, len(td)):
                    if td[j].find("span") == None:
                        formatted_data[j] = td[j].string.strip()
                    else:
                        formatted_data[j] = td[j].find("span").string.strip()
                formatted_data.append(datetime.datetime.now())
                player_list.append(tuple(formatted_data))

    return player_list

def push_to_db(player_list, conn):
    insert_sql = """INSERT INTO public.hashtag_rankings_stats (rank, name, pos, team, gp, mpg, fg_percent, ft_percent, threepm, pts, treb, ast, stl, blk, turnovers, total, last_modified) VALUES(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s);"""
    delete_sql = """DELETE FROM public.hashtag_rankings_stats"""
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
