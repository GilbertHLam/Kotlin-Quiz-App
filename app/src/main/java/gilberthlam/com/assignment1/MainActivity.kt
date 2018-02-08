package gilberthlam.com.assignment1
import android.annotation.SuppressLint
import android.app.AlertDialog
import kotlinx.android.synthetic.main.content_quiz.*
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import gilberthlam.com.assignment1.R.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList
import android.widget.Toast
import kotlinx.android.synthetic.main.submit.view.*
import android.content.Intent
import android.text.Editable
import android.view.View


@Suppress("PLUGIN_WARNING")
class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName + " @" + System.identityHashCode(this)
    private val STATE_questionIndex = "STATE_questionIndex"
    private var mQuestionIndex: Int = 0




    private var mQuestions: ArrayList<Question>? = null


    @SuppressLint("InflateParams")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate(Bundle)")
        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)

        //When the buttons are pressed, it'll change the question and call updateUI
        //=================================================================================
        prev_button!!.setOnClickListener({
            Log.i(TAG, "Prev Button Clicked")

            if (mQuestionIndex > 0) mQuestionIndex--

            updateUI()

        })
        next_button!!.setOnClickListener({
            Log.i(TAG, "Next Button Clicked")

            if (mQuestionIndex < mQuestions!!.size - 1) mQuestionIndex++

            updateUI()

        })
        //=================================================================================

        //Submit button
        fab!!.setOnClickListener({
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.submit, null)

            dialogBuilder.setView(dialogView)
                    // Add action buttons
                    .setPositiveButton(R.string.submit, { _, _ ->
                        val userName = dialogView.email.text
                        val studentID = dialogView.studentid.text
                        //Checks if fields are filled
                        if(userName != null && studentID != null){
                            val emailAddress = Exam.getEmail()
                            //Creates the email intent and prompts user to select an app
                            val emailIntent = Intent(Intent.ACTION_SEND)
                            emailIntent.type = "message/rfc822"
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "COMP 2601")
                            emailIntent.putExtra(Intent.EXTRA_TEXT, createBody(userName, studentID))
                            try {
                                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                                finish()

                            } catch (ex: android.content.ActivityNotFoundException) {
                                Toast.makeText(this@MainActivity,
                                        "There is no email client installed.", Toast.LENGTH_SHORT).show()
                            }

                        }
                        else {
                            Toast.makeText(this, "Make sure you have a valid email!",
                                    Toast.LENGTH_LONG).show()
                        }
                    })
                    .setNegativeButton(R.string.cancel, { dialog, _ ->
                        dialog.cancel()
                    })

            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        })
        //When the choice buttons are pressed, the userChoice for the question is set and updateUI is called
        //============================================================================
        a_button!!.setOnClickListener({
            Log.i(TAG, "A Button Clicked")

            mQuestions!![mQuestionIndex].setUserChoice("A")

            updateUI()
        })
        b_button!!.setOnClickListener({
            Log.i(TAG, "B Button Clicked")

            mQuestions!![mQuestionIndex].setUserChoice("B")

            updateUI()
        })
        c_button!!.setOnClickListener({
            Log.i(TAG, "C Button Clicked")

            mQuestions!![mQuestionIndex].setUserChoice("C")

            updateUI()
        })
        d_button!!.setOnClickListener({
            Log.i(TAG, "D Button Clicked")

            mQuestions!![mQuestionIndex].setUserChoice("D")

            updateUI()
        })
        e_button!!.setOnClickListener({
            Log.i(TAG, "E Button Clicked")

            mQuestions!![mQuestionIndex].setUserChoice("E")

            updateUI()
        })
        //============================================================================

        //Gets the Questions from the XML file and puts it into the model arraylist
        var parsedModel: ArrayList<Question>? = null
        try {
            val iStream = resources.openRawResource(raw.questions)
            val bReader = BufferedReader(InputStreamReader(iStream))
            val aStream = resources.openRawResource(raw.answers)
            val cReader = BufferedReader(InputStreamReader(aStream))
            parsedModel = Exam.pullParseFrom(bReader,cReader)
            bReader.close()
        } catch (e: java.io.IOException) {
            e.printStackTrace()

        }

        if (parsedModel == null || parsedModel.isEmpty())
            Log.i(TAG, "ERROR: Questions Not Parsed")
        mQuestions = parsedModel
        if (savedInstanceState != null) mQuestionIndex = savedInstanceState.getInt(STATE_questionIndex)
        updateUI()

    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart()")
        //activity paused but visible to user, not in foreground

    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume()")
        //activity is visible, running and in foreground

    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause()")
        //activity paused, visible, not in foreground

    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop()")
        //activity stopped, not visible
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy()")
        //activity and will be destroyed
    }

    //Overide saving and restoring of activity instance state data
    public override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        //called after onPause()
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState()")
        //save current question index.
        savedInstanceState!!.putInt(STATE_questionIndex, mQuestionIndex)

    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        //Gets called after onStart();
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState()")
        //recover state from saved instance state
        if (savedInstanceState != null) mQuestionIndex = savedInstanceState.getInt(STATE_questionIndex)

    }

    private fun updateUI() {
        //update UI views based on state of model objects
        if (mQuestions != null && mQuestions!!.size > 0) {

            if (mQuestionIndex > mQuestions!!.size)
                mQuestionIndex = 0
            //Only show the next/prevous buttons if there are next/previous questions
            when (mQuestionIndex) {
                0 -> prev_button.visibility = View.INVISIBLE
                mQuestions!!.size - 1 -> next_button.visibility = View.INVISIBLE
                else -> {
                    prev_button.visibility = View.VISIBLE
                    next_button.visibility = View.VISIBLE
                }
            }

            question_text_view.text = ("(" + (mQuestionIndex + 1) + ") " +
                    mQuestions!![mQuestionIndex].getQuestion())

            aChoice.text = mQuestions!![mQuestionIndex].getChoiceA()
            bChoice.text = mQuestions!![mQuestionIndex].getChoiceB()
            cChoice.text = mQuestions!![mQuestionIndex].getChoiceC()
            dChoice.text = mQuestions!![mQuestionIndex].getChoiceD()
            eChoice.text = mQuestions!![mQuestionIndex].getChoiceE()

            //Sets the user buttons based on what is selected
            when(mQuestions!![mQuestionIndex].getUserChoice()){
                "A"->{
                    a_button.setBackgroundResource(android.R.drawable.btn_star_big_on)
                    b_button.setBackgroundResource(android.R.drawable.btn_default)
                    c_button.setBackgroundResource(android.R.drawable.btn_default)
                    d_button.setBackgroundResource(android.R.drawable.btn_default)
                    e_button.setBackgroundResource(android.R.drawable.btn_default)
                }
                "B"->{
                    b_button.setBackgroundResource(android.R.drawable.btn_star_big_on)
                    a_button.setBackgroundResource(android.R.drawable.btn_default)
                    c_button.setBackgroundResource(android.R.drawable.btn_default)
                    d_button.setBackgroundResource(android.R.drawable.btn_default)
                    e_button.setBackgroundResource(android.R.drawable.btn_default)
                }
                "C"->{
                    c_button.setBackgroundResource(android.R.drawable.btn_star_big_on)
                    b_button.setBackgroundResource(android.R.drawable.btn_default)
                    a_button.setBackgroundResource(android.R.drawable.btn_default)
                    d_button.setBackgroundResource(android.R.drawable.btn_default)
                    e_button.setBackgroundResource(android.R.drawable.btn_default)
                }
                "D"->{
                    d_button.setBackgroundResource(android.R.drawable.btn_star_big_on)
                    b_button.setBackgroundResource(android.R.drawable.btn_default)
                    c_button.setBackgroundResource(android.R.drawable.btn_default)
                    a_button.setBackgroundResource(android.R.drawable.btn_default)
                    e_button.setBackgroundResource(android.R.drawable.btn_default)
                }
                "E"->{
                    e_button.setBackgroundResource(android.R.drawable.btn_star_big_on)
                    b_button.setBackgroundResource(android.R.drawable.btn_default)
                    c_button.setBackgroundResource(android.R.drawable.btn_default)
                    d_button.setBackgroundResource(android.R.drawable.btn_default)
                    a_button.setBackgroundResource(android.R.drawable.btn_default)
                }
                else->{
                    e_button.setBackgroundResource(android.R.drawable.btn_default)
                    b_button.setBackgroundResource(android.R.drawable.btn_default)
                    c_button.setBackgroundResource(android.R.drawable.btn_default)
                    d_button.setBackgroundResource(android.R.drawable.btn_default)
                    a_button.setBackgroundResource(android.R.drawable.btn_default)
                }
            }

        }

    }

    private fun createBody(studentName: Editable?, studentID: Editable?) : String {
        //Constucts the body of the email by going through the Question list and grabbing the questions and the user's choices
        var body = "Student ID: $studentID\nStudent Name: $studentName\n\n"
        for (item in mQuestions!!){
            body += "\n\n\nQuestion: ${item.getQuestion()}\n\nStudent Choice: ${item.getUserChoice()}\n"
        }
        return body
    }


}
