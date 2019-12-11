# 6.0001/6.00 Problem Set 5 - RSS Feed Filter

import feedparser
import string
import time
import threading
from project_util import translate_html
from datetime import datetime
import pytz


#-----------------------------------------------------------------------

#======================
# Code for retrieving and parsing
# Google and Yahoo News feeds
# Do not change this code
#======================

def process(url):
    """
    Fetches news items from the rss url and parses them.
    Returns a list of NewsStory-s.
    """
    feed = feedparser.parse(url)
    entries = feed.entries
    ret = []
    for entry in entries:
        guid = entry.guid
        title = translate_html(entry.title)
        link = entry.link
        description = translate_html(entry.description)
        pubdate = translate_html(entry.published)

        try:
            pubdate = datetime.strptime(pubdate, "%a, %d %b %Y %H:%M:%S %Z")
            pubdate.replace(tzinfo=pytz.timezone("GMT"))
          #  pubdate = pubdate.astimezone(pytz.timezone('EST'))
          #  pubdate.replace(tzinfo=None)
        except ValueError:
            pubdate = datetime.strptime(pubdate, "%a, %d %b %Y %H:%M:%S %z")

        newsStory = NewsStory(guid, title, description, link, pubdate)
        ret.append(newsStory)
    return ret

#======================
# Data structure design
#======================

# Problem 1
class NewsStory(object):
    def __init__(self, guid, title, description, link, pubdate):
        self.guid = guid
        self.title = title
        self.description = description
        self.link = link
        self.pubdate = pubdate
    
    def get_guid(self):
        return self.guid
    
    def get_title(self):
        return self.title
    
    def get_description(self):
        return self.description

    def get_link(self):
        return self.link
    
    def get_pubdate(self):
        return self.pubdate
        

#======================
# Triggers
#======================

class Trigger(object):
    def evaluate(self, story):
        """
        Returns True if an alert should be generated
        for the given news item, or False otherwise.
        """
        # DO NOT CHANGE THIS!
        raise NotImplementedError

# PHRASE TRIGGERS

# Problem 2
class PhraseTrigger(Trigger):
    def __init__(self, phrase):
        self.phrase = phrase.lower().split(' ')
    
    def is_phrase_in(self, text):
        # replace all special characters
        for c in text:
            if c in string.punctuation:
                text = text.replace(c, ' ')
        
        # convert text into a list of words
        words = [w.lower() for w in text.split(' ') if w is not '']

        # evaluate if phrase is consecutively in text
        for i, word in enumerate(words):
            trues = 0
            for j, p in enumerate(self.phrase):
                if i+j >= len(words):
                    return False
                elif words[i+j] == p:
                    trues += 1
                    if trues == len(self.phrase):
                        return True
                else:
                    break
        return False

# Problem 3
class TitleTrigger(PhraseTrigger):
    def __init__(self, phrase):
        super().__init__(phrase)
    
    def evaluate(self, story):
        return super().is_phrase_in(story.get_title())

# Problem 4
class DescriptionTrigger(PhraseTrigger):
    def __init__(self, phrase):
        super().__init__(phrase)
    
    def evaluate(self, story):
        return super().is_phrase_in(story.get_description())

# TIME TRIGGERS

# Problem 5
class TimeTrigger(Trigger):
    def __init__(self, time):
        self.time = datetime.strptime(time, "%d %b %Y %H:%M:%S")

# Problem 6

class BeforeTrigger(TimeTrigger):
    def __init__(self, time):
        super().__init__(time)

    def evaluate(self, story):
        pubdate = story.get_pubdate().replace(tzinfo=pytz.UTC)
        trigdate = self.time.replace(tzinfo=pytz.UTC)
        return (True if trigdate > pubdate else False)

class AfterTrigger(TimeTrigger):
    def __init__(self, time):
        super().__init__(time)

    def evaluate(self, story):
        pubdate = story.get_pubdate().replace(tzinfo=pytz.UTC)
        trigdate = self.time.replace(tzinfo=pytz.UTC)
        return (True if trigdate < pubdate else False)

# COMPOSITE TRIGGERS

# Problem 7
class NotTrigger(Trigger):
    def __init__(self, T):
        self.T = T
    
    def evaluate(self, story):
        return not self.T.evaluate(story)

# Problem 8
class AndTrigger(Trigger):
    def __init__(self, S, T):
        self.S = S 
        self.T = T
    
    def evaluate(self, story):
        return self.S.evaluate(story) and self.T.evaluate(story)

# Problem 9
class OrTrigger(Trigger):
    def __init__(self, S, T):
        self.S = S 
        self.T = T
    
    def evaluate(self, story):
        return self.S.evaluate(story) or self.T.evaluate(story)

#======================
# Filtering
#======================

# Problem 10
def filter_stories(stories, triggerlist):
    """
    Takes in a list of NewsStory instances.

    Returns: a list of only the stories for which a trigger in triggerlist fires.
    """

    storylist = list()

    for story in stories:
        for T in triggerlist:
            if T.evaluate(story):
                storylist.append(story)

    return storylist

#======================
# User-Specified Triggers
#======================

# Problem 11
def read_trigger_config(filename):
    """
    filename: the name of a trigger configuration file

    Returns: a list of trigger objects specified by the trigger configuration
        file.
    """
    # We give you the code to read in the file and eliminate blank lines and
    # comments. You don't need to know how it works for now!
    trigger_file = open(filename, 'r')
    lines = []
    for line in trigger_file:
        line = line.rstrip()
        if not (len(line) == 0 or line.startswith('//')):
            lines.append(line)

    trigger_list = list()
    trigger_maps = dict()
    trigger_defs = {
        'TITLE': TitleTrigger,
        'DESCRIPTION': DescriptionTrigger,
        'AFTER': AfterTrigger,
        'BEFORE': BeforeTrigger,
        }
    trigger_composite = {
        'NOT': NotTrigger,
        'AND': AndTrigger,
        'OR': OrTrigger
        }

    for line in lines:
        command = line.split(',')
        # add triggers to trigger_list
        if command[0] == 'ADD':
            for tname in command[1:]:
                trigger_list.append(trigger_maps[tname])
        # construct trigger_maps
        elif command[1] in trigger_composite.keys(): # handle composite triggers
            trigger_maps[command[0]] = trigger_composite[command[1]](trigger_maps[command[2]], trigger_maps[command[3]])
        
        else: # handle ordinary triggers
            trigger_maps[command[0]] = trigger_defs[command[1]](command[2])
    
    return trigger_list

SLEEPTIME = 60 #seconds -- how often we poll

def main_thread():
    # A sample trigger list - you might need to change the phrases to correspond
    # to what is currently in the news
    try:
        # t1 = TitleTrigger("election")
        # t2 = DescriptionTrigger("Trump")
        # t3 = DescriptionTrigger("Clinton")
        # t4 = AndTrigger(t2, t3)
        # triggerlist = [t1, t4]
        
        # read trigger from config file instead of defining them above
        triggerlist = read_trigger_config('triggers.txt')
        
        # Problem 12
        # triggerlist = read_trigger_config('debate_triggers.txt')

        guidShown = []
        def get_cont(newstory):
            if newstory.get_guid() not in guidShown:
                print(newstory.get_title()+"\n")
                print("\n---------------------------------------------------------------\n")
                print(newstory.get_description())
                print("\n*********************************************************************\n")
                guidShown.append(newstory.get_guid())

        while True:

            print("Polling . . .", end=' ')
            # Get stories from Google's Top Stories RSS news feed
            stories = process("http://news.google.com/news?output=rss")

            # Get stories from Yahoo's Top Stories RSS news feed
            stories.extend(process("http://news.yahoo.com/rss/topstories"))

            stories = filter_stories(stories, triggerlist)

            list(map(get_cont, stories))
            # scrollbar.config(command=cont.yview)


            print("Sleeping...")
            time.sleep(SLEEPTIME)

    except Exception as e:
        print(e)


if __name__ == '__main__':
    main_thread()