package gilberthlam.com.assignment1

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.util.ArrayList


object Exam {


    //XML tags used to define an exam of multiple choice questions.
    private const val XML_EMAIL = "email"
    private const val XML_QUESTION = "question"
    private const val XML_QUESTION_TEXT = "question_text"
    private const val XML_A = "A"
    private const val XML_B = "B"
    private const val XML_C = "C"
    private const val XML_D = "D"
    private const val XML_E = "E"
    private const val XML_ANSWER = "answer"
    private var examEmail: String? = null
    private var listOfAnswers: ArrayList<String>? = null


    //This function reads the XML file and puts all the answers to the questions into an arrayList
    private fun pullAnswerFrom(reader: BufferedReader): ArrayList<String> {

        val answerList = ArrayList<String>()
        val factory: XmlPullParserFactory
        try {
            factory = XmlPullParserFactory.newInstance()
            val xpp = factory.newPullParser()
            xpp.setInput(reader)
            var eventType = xpp.eventType
            var currentText = ""
            var currentAnswer: String

            while(eventType != XmlPullParser.END_DOCUMENT){
                val tagname = xpp.name
                when(eventType) {
                    XmlPullParser.TEXT ->
                            currentText = xpp.text.trim { it <= ' ' }
                    XmlPullParser.END_TAG ->
                        if (tagname.equals(XML_ANSWER, ignoreCase = true)) {
                            currentAnswer = currentText
                            //Adds the answer to the list
                            answerList.add(currentAnswer)
                        }
                    else -> {
                    }

                }
                eventType = xpp.next()
            }

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: java.io.IOException) {
            e.printStackTrace()
        }
        return answerList
    }
    fun pullParseFrom(reader: BufferedReader, answerReader: BufferedReader): ArrayList<Question> {
        val questions = ArrayList<Question>()
        listOfAnswers = pullAnswerFrom(answerReader)
        // Get our factory and create a PullParser
        val factory: XmlPullParserFactory?
        try {
            factory = XmlPullParserFactory.newInstance()
            val xpp = factory!!.newPullParser()
            xpp.setInput(reader) // set input file for parser
            var eventType = xpp.eventType // get initial eventType

            var currentText = "" //current text between XML tags being parsed
            var currentQuestion: Question? //current question object being parsed
            var currentQuestionText: String? = null //text of the question being parsed
            var currentQuestionA: String? = null
            var currentQuestionB: String? = null
            var currentQuestionC: String? = null
            var currentQuestionD: String? = null
            var currentQuestionE: String? = null
            var currentQuestionAnswer: String?
            var currentQuestionIndex = 0

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the current tag
                val tagname = xpp.name

                // handle the various xml tags encounted
                when (eventType) {
                    XmlPullParser.TEXT ->
                        //get text between xml tags
                        currentText = xpp.text.trim { it <= ' ' }

                    XmlPullParser.END_TAG //XML closing tags
                    -> if (tagname.equals(XML_QUESTION_TEXT, ignoreCase = true)) {
                        //end of a question's text </question_text>
                        currentQuestionText = currentText
                    } else if (tagname.equals(XML_A, ignoreCase = true)) {
                        // </A>
                        currentQuestionA = currentText
                    } else if (tagname.equals(XML_B, ignoreCase = true)) {
                        // </B>
                        currentQuestionB = currentText
                    } else if (tagname.equals(XML_C, ignoreCase = true)) {
                        // </C>
                        currentQuestionC = currentText
                    } else if (tagname.equals(XML_D, ignoreCase = true)) {
                        // </D>
                        currentQuestionD = currentText
                    } else if (tagname.equals(XML_E, ignoreCase = true)) {
                        // </E>
                        currentQuestionE = currentText
                    } else if (tagname.equals(XML_EMAIL, ignoreCase = true)) {
                        // </email>
                        examEmail = currentText
                    }  else if (tagname.equals(XML_QUESTION, ignoreCase = true)) {
                        // </question>
                        currentQuestionAnswer = listOfAnswers!![currentQuestionIndex]
                        currentQuestionIndex++
                        //Create a new QUestion object and add it to the list of questions
                        currentQuestion = Question(currentQuestionText!!,
                                currentQuestionA!!,
                                currentQuestionB!!,
                                currentQuestionC!!,
                                currentQuestionD!!,
                                currentQuestionE!!,
                                currentQuestionAnswer)
                        questions.add(currentQuestion)
                    }

                    else -> {
                    }
                }
                //iterate
                eventType = xpp.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: java.io.IOException) {
            e.printStackTrace()
        }

        return questions

    }
    //This function returns the email found in the XML file
    fun getEmail(): String?{
        return examEmail
    }



}