package edu.moravian.csci215.AndroidMusicPlayer

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

private const val DATABASE_NAME = "lyrics-database"
class LyricsRepository private constructor(context: Context) {
    private val coroutineScope: CoroutineScope = GlobalScope

    /**
     * Room.databaseBuilder() creates a concrete implementation of the abstract MusicDatabase
     * @param context.applicationContext to access the filesystem
     * @param NodeDatabase::class.java database room creates
     * @param DATABASE_NAME name of the database
     */
    private val database: LyricsDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            LyricsDatabase::class.java,
            DATABASE_NAME
        )
        // .createFromAsset(DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    /**
     * function to insert lyrics into the database
     * @param lyrics
     */
    suspend fun insertLyrics(lyrics: Lyrics) = database.lyricsDao().insertLyrics(lyrics)

    /**
     * function for the getLyricsById function in HierarchyDAO
     * So other components can preform any operations they need on the database
     * @param lyricsId
     */
    suspend fun getLyricsById(lyricsId: UUID): Lyrics = database.lyricsDao().getLyricsById(lyricsId)

    companion object {
        private var INSTANCE: LyricsRepository? = null

        /**
         * Initializes a new instance of the repository
         * @param context
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                println(context)
                INSTANCE = LyricsRepository(context)
                println(INSTANCE)
                println(INSTANCE?.database)
            }

//            GlobalScope.launch {
//                INSTANCE?.insertLyrics(
//                    Lyrics(
//                        lyricsId = UUID.fromString("ccc726c1-1d0d-48f0-b0d3-826eaa81f61b"),
//                        lyrics = "oo ooo ahh ahh"
//                    )
//                )
//                INSTANCE?.insertLyrics(
//                    Lyrics(
//                        lyricsId = UUID.fromString("7c43c391-fd7d-48b8-92a1-1cb327498632"),
//                        lyrics = "Every time when I look in the mirror\n" +
//                                "All these lines on my face getting clearer\n" +
//                                "The past is gone\n" +
//                                "And it went by, like dusk to dawn\n" +
//                                "Isn't that the way?\n" +
//                                "Everybody's got their dues in life to pay\n" +
//                                "Yeah, I know nobody knows\n" +
//                                "Where it comes and where it goes\n" +
//                                "I know it's everybody's sin\n" +
//                                "You got to lose to know how to win\n" +
//                                "Half my life's in books, written pages\n" +
//                                "Live and learn from fools and from sages\n" +
//                                "You know it's true, oh\n" +
//                                "All the things come back to you\n" +
//                                "Sing with me, sing for a year\n" +
//                                "Sing for the laughter, and sing the tear\n" +
//                                "Sing with me, if it's just for today\n" +
//                                "Maybe tomorrow, the good Lord will take you away\n" +
//                                "Yeah, sing with me, sing for the year\n" +
//                                "Sing for the laughter, and sing for the tear\n" +
//                                "Sing it with me, if it's just for today\n" +
//                                "Maybe tomorrow, the good Lord will take you away\n" +
//                                "Dream on\n" +
//                                "Dream on\n" +
//                                "Dream on\n" +
//                                "Dream until the dream come true\n" +
//                                "Dream on\n" +
//                                "Dream on\n" +
//                                "Dream on\n" +
//                                "Dream until your dream come true\n" +
//                                "Dream on\n" +
//                                "Dream on\n" +
//                                "Dream on\n" +
//                                "Dream on\n" +
//                                "Dream on\n" +
//                                "Dream on\n" +
//                                "Dream on, ah\n" +
//                                "Sing with me, sing for the year\n" +
//                                "Sing for the laughter and sing for the tear\n" +
//                                "Sing with me, if it's just for today\n" +
//                                "Maybe tomorrow, the good Lord will take you away\n" +
//                                "Sing with me, sing for the year\n" +
//                                "Sing for the laughter, and sing for the tear\n" +
//                                "Sing it with me, if it's just for today\n" +
//                                "Maybe tomorrow, the good Lord will take you away"
//                    )
//                )
//
//                INSTANCE?.insertLyrics(
//                    Lyrics(
//                        lyricsId = UUID.fromString("cf5ecc77-b8ca-4bbd-a4c6-6ece4af287ab"),
//                        lyrics = "He was a-\n" +
//                                "Ya know it\n" +
//                                "He was a-\n" +
//                                "I was walking down the street\n" +
//                                "When out the corner of my eye\n" +
//                                "I saw a pretty little thing approaching me\n" +
//                                "She said, I've never seen a man\n" +
//                                "Who looks so all alone\n" +
//                                "Uh, could you use a little company?\n" +
//                                "If you pay the right price\n" +
//                                "Your evening will be nice\n" +
//                                "And you can go and send me on my way\n" +
//                                "I said, \"You're such a sweet young thing\n" +
//                                "Why'd you do this to yourself?\"\n" +
//                                "She looked at me and this is what she said\n" +
//                                "\"Oh, there ain't no rest for the wicked\n" +
//                                "Money don't grow on trees\n" +
//                                "I got bills to pay\n" +
//                                "I got mouths to feed\n" +
//                                "There ain't nothing in this world for free\n" +
//                                "I know I can't slow down\n" +
//                                "I can't hold back\n" +
//                                "Though you know\n" +
//                                "I wish I could\n" +
//                                "Oh, no there ain't no rest for the wicked\n" +
//                                "Until we close our eyes for good\"\n" +
//                                "Not even 15 minutes later\n" +
//                                "I'm still walking down the street\n" +
//                                "When I saw the shadow of a man creep out of sight\n" +
//                                "And then he swept up from behind\n" +
//                                "He put a gun up to my head\n" +
//                                "He made it clear he wasn't looking for a fight\n" +
//                                "He said, \"Give me all you've got\n" +
//                                "I want your money not your life\n" +
//                                "But if you try to make a move, I won't think twice\"\n" +
//                                "I told him, \"You can have my cash\n" +
//                                "But first you know I got to ask\n" +
//                                "What made you want to live this kind of life?\"\n" +
//                                "He said, \"There ain't no rest for the wicked\n" +
//                                "Money don't grow on trees\n" +
//                                "I got bills to pay\n" +
//                                "I got mouths to feed\n" +
//                                "There ain't nothing in this world for free\n" +
//                                "I know I can't slow down\n" +
//                                "I can't hold back\n" +
//                                "Though you know, I wish I could\n" +
//                                "Oh no there ain't no rest for the wicked\n" +
//                                "Until we close our eyes for good\"\n" +
//                                "Yeah\n" +
//                                "You know it\n" +
//                                "He was a-\n" +
//                                "You know it\n" +
//                                "He was a-\n" +
//                                "Well, now a couple hours passed\n" +
//                                "And I was sitting at my house\n" +
//                                "The day was winding down and coming to an end\n" +
//                                "And so I turned on the TV\n" +
//                                "And flipped it over to the news\n" +
//                                "And what I saw I almost couldn't comprehend\n" +
//                                "I saw a preacher man in cuffs\n" +
//                                "He'd taken money from the church\n" +
//                                "He'd stuffed his bank account with righteous dollar bills\n" +
//                                "But even still I can't say much\n" +
//                                "Because I know we're all the same\n" +
//                                "Oh yes, we all seek out to satisfy those thrills\n" +
//                                "You know there ain't no rest for the wicked\n" +
//                                "Money don't grow on trees\n" +
//                                "We got bills to pay\n" +
//                                "We got mouths to feed\n" +
//                                "There ain't nothing in this world for free\n" +
//                                "I know we can't slow down\n" +
//                                "We can't hold back, though you know, we wish we could\n" +
//                                "Oh no, there ain't no rest for the wicked\n" +
//                                "Until we close our eyes for good"))
//
//                INSTANCE?.insertLyrics(
//                    Lyrics(
//                        lyricsId = UUID.fromString("6f44e776-a740-4422-be96-6e61e639c1dc"),
//                        lyrics = "Just a small town girl\n" +
//                                "Livin' in a lonely world\n" +
//                                "She took the midnight train going anywhere\n" +
//                                "Just a city boy\n" +
//                                "Born and raised in South Detroit\n" +
//                                "He took the midnight train going anywhere\n" +
//                                "A singer in a smokey room\n" +
//                                "A smell of wine and cheap perfume\n" +
//                                "For a smile they can share the night\n" +
//                                "It goes on and on and on and on\n" +
//                                "Strangers waitin'\n" +
//                                "Up and down the boulevard\n" +
//                                "Their shadows searchin' in the night\n" +
//                                "Streetlights, people\n" +
//                                "Livin' just to find emotion\n" +
//                                "Hidin', somewhere in the night\n" +
//                                "Workin' hard to get my fill\n" +
//                                "Everybody wants a thrill\n" +
//                                "Payin' anything to roll the dice\n" +
//                                "Just one more time\n" +
//                                "Some'll win, some will lose\n" +
//                                "Some are born to sing the blues\n" +
//                                "Whoa, the movie never ends\n" +
//                                "It goes on and on and on and on\n" +
//                                "Strangers waitin'\n" +
//                                "Up and down the boulevard\n" +
//                                "Their shadows searchin' in the night\n" +
//                                "Streetlights, people\n" +
//                                "Livin' just to find emotion\n" +
//                                "Hidin', somewhere in the night\n" +
//                                "Don't stop believin'\n" +
//                                "Hold on to that feelin'\n" +
//                                "Streetlights, people\n" +
//                                "Don't stop believin'\n" +
//                                "Hold on\n" +
//                                "Streetlights, people\n" +
//                                "Don't stop believin'\n" +
//                                "Hold on to that feelin'\n" +
//                                "Streetlights, people"))
//
//                INSTANCE?.insertLyrics(
//                    Lyrics(
//                        lyricsId = UUID.fromString("02f0d4f8-56e2-4c2a-bfed-e73328aa1559"),
//                        lyrics = "Mm, oh, hey, Ratatat\n" +
//                                "Yeah, na-na-na-na\n" +
//                                "Na-na-na-na\n" +
//                                "Crush a bit, little bit\n" +
//                                "Roll it up, take a hit\n" +
//                                "Feeling lit, feeling right\n" +
//                                "Two AM, summer night, I don't care\n" +
//                                "Hand on the wheel\n" +
//                                "Driving drunk I'm doing my thang\n" +
//                                "Rolling the Midwest side and out\n" +
//                                "Living my life, getting our dreams\n" +
//                                "People told me slow my roll\n" +
//                                "I'm screaming out, \"Fuck that\"\n" +
//                                "I'ma do just what I want\n" +
//                                "Looking ahead no turning back\n" +
//                                "If I fall if I die\n" +
//                                "Know I lived it to the fullest\n" +
//                                "If I fall if I die\n" +
//                                "Know I lived and missed some bullets\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "Tell me what you know about dreamin', dreamin'\n" +
//                                "You don't really know about nothin', nothin'\n" +
//                                "Tell me what you know about them night terrors every night\n" +
//                                "Five AM cold sweats, waking up to the sky\n" +
//                                "Tell me what you know about dreams, dreams\n" +
//                                "Tell me what you know about night terrors, nothin'\n" +
//                                "You don't really care about the trials of tomorrow\n" +
//                                "Rather lay awake in the bed full of sorrow\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "I'm on the pursuit of happiness\n" +
//                                "I know everything that shine ain't always gold\n" +
//                                "I'll be fine once I get it, I'll be good\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "Pursuit of happiness\n" +
//                                "Yeah, I'm gon' get it, I'll be, good\n" +
//                                "Ugh\n" +
//                                "Oh man\n" +
//                                "Oh alright, oh\n" +
//                                "Room's spinning, room's spinning\n" +
//                                "Pat, Zuli, wait, oh fuck\n" +
//                                "Oh my God\n" +
//                                "Why did I drink so much and smoke so much? Ugh\n" +
//                                "Oh fuck"))
//
//                INSTANCE?.insertLyrics(
//                    Lyrics(
//                        lyricsId = UUID.fromString("b5a663b5-8aef-4658-91e0-7890c0b0a8c4"),
//                        lyrics = "Mm, oh, hey, Ratatat\n" +
//                                "Yeah, na-na-na-na\n" +
//                                "Na-na-na-na\n" +
//                                "Crush a bit, little bit\n" +
//                                "Roll it up, take a hit\n" +
//                                "Feeling lit, feeling right\n" +
//                                "Two AM, summer night, I don't care\n" +
//                                "Hand on the wheel\n" +
//                                "Driving drunk I'm doing my thang\n" +
//                                "Rolling the Midwest side and out\n" +
//                                "Living my life, getting our dreams\n" +
//                                "People told me slow my roll\n" +
//                                "I'm screaming out, \"Fuck that\"\n" +
//                                "I'ma do just what I want\n" +
//                                "Looking ahead no turning back\n" +
//                                "If I fall if I die\n" +
//                                "Know I lived it to the fullest\n" +
//                                "If I fall if I die\n" +
//                                "Know I lived and missed some bullets\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "Tell me what you know about dreamin', dreamin'\n" +
//                                "You don't really know about nothin', nothin'\n" +
//                                "Tell me what you know about them night terrors every night\n" +
//                                "Five AM cold sweats, waking up to the sky\n" +
//                                "Tell me what you know about dreams, dreams\n" +
//                                "Tell me what you know about night terrors, nothin'\n" +
//                                "You don't really care about the trials of tomorrow\n" +
//                                "Rather lay awake in the bed full of sorrow\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "I'm on the pursuit of happiness\n" +
//                                "I know everything that shine ain't always gold\n" +
//                                "I'll be fine once I get it, I'll be good\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "I'm on the pursuit of happiness and I know\n" +
//                                "Everything that shine ain't always gonna be gold, hey\n" +
//                                "I'll be fine once I get it, yeah, I'll be good\n" +
//                                "Pursuit of happiness\n" +
//                                "Yeah, I'm gon' get it, I'll be, good\n" +
//                                "Ugh\n" +
//                                "Oh man\n" +
//                                "Oh alright, oh\n" +
//                                "Room's spinning, room's spinning\n" +
//                                "Pat, Zuli, wait, oh fuck\n" +
//                                "Oh my God\n" +
//                                "Why did I drink so much and smoke so much? Ugh\n" +
//                                "Oh fuck"))
//
//                INSTANCE?.insertLyrics(
//                    Lyrics(
//                        lyricsId = UUID.fromString("9fa5186d-97dd-4e6a-9f86-857c284027b4"),
//                        lyrics = "Oh, oo-o-oh, come on, ooh, yeah\n" +
//                                "Well I tried to tell you so (yes, I did)\n" +
//                                "But I guess you didn't know, as I said the story goes\n" +
//                                "Baby, now I got the flow\n" +
//                                "'Cos I knew it from the start\n" +
//                                "Baby, when you broke my heart\n" +
//                                "That I had to come again, and show you that I'm real\n" +
//                                "all those times I said that I love you\n" +
//                                "(You lied to me) yes, I tried, yes, I tried\n" +
//                                "(You lied to me) even though you know I'd die for you\n" +
//                                "(You lied to me) yes, I cried, yes, I cried\n" +
//                                "1-(Return of the Mack) it is\n" +
//                                "(Return of the Mack) come on\n" +
//                                "(Return of the Mack) oh my God\n" +
//                                "(You know that I'll be back) here I am\n" +
//                                "(Return of the Mack) once again\n" +
//                                "(Return of the Mack) pump up the world\n" +
//                                "(Return of the Mack) watch my flow\n" +
//                                "(You know that I'll be back) here I go\n" +
//                                "So I'm back up in the game\n" +
//                                "Running things to keep my swing\n" +
//                                "Letting all the people know\n" +
//                                "That I'm back to run the show\n" +
//                                "'Cos what you did, you know, was wrong\n" +
//                                "And all the nasty things you've done\n" +
//                                "So, baby, listen carefully\n" +
//                                "While I sing my come-back song\n" +
//                                "2-(You lied to me) 'cos she said she'd never turn on me\n" +
//                                "(You lied to me) but you did, but you do\n" +
//                                "(You lied to me) all these pains you said I'd never feel\n" +
//                                "(You lied to me) but I do, but I do, do, do\n" +
//                                "here it is\n" +
//                                "(Return of the Mack) hold on\n" +
//                                "(Return of the Mack) don't you know\n" +
//                                "(You know that I'll be back) here I go\n" +
//                                "(Return of the Mack) oh little girl\n" +
//                                "(Return of the Mack) wants my pearl\n" +
//                                "(Return of the Mack) up and down\n" +
//                                "(You know that I'll be back) round and round\n" +
//                                "(Rpt 2, 1)\n" +
//                                "don't you know\n" +
//                                "here it is\n" +
//                                "(Return of the Mack) hold on\n" +
//                                "(Return of the Mack) be strong\n" +
//                                "(You know that I'll be back) here I go\n" +
//                                "(Return of the Mack) my little girl\n" +
//                                "(Return of the Mack) wants my pearl\n" +
//                                "(Return of the Mack) up and down\n" +
//                                "(You know that I'll be back) round and round"))
//
//            }
        }

        /**
         * getter function that throws an exception if the initialize() wasn't called before it
         * @return repository returns a non null repository instance
         */
        fun get(): LyricsRepository {
            return INSTANCE ?: throw IllegalStateException("LyricsRepository must be initialized")
        }
    }
}