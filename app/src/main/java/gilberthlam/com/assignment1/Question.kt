package gilberthlam.com.assignment1


/**
 * Created by Gilbert Lam on 2018-02-04.
 */
class Question(questionString: String, choiceA: String, choiceB: String, choiceC: String, choiceD: String, choiceE: String, answer: String) {

    private var mQuestionString: String? = null
    private var mChoiceA: String? = null
    private var mChoiceB: String? = null
    private var mChoiceC: String? = null
    private var mChoiceD: String? = null
    private var mChoiceE: String? = null
    private var mAnswer: String? = null
    private var mUserChoice : String? = null

    init{
        mQuestionString = questionString
        mChoiceA = choiceA
        mChoiceB = choiceB
        mChoiceC = choiceC
        mChoiceD = choiceD
        mChoiceE = choiceE
        mAnswer = answer
    }



    fun getUserChoice(): String? {
        return mUserChoice
    }

    fun getQuestion(): String? {
        return mQuestionString
    }

    fun getChoiceA(): String? {
        return mChoiceA
    }

    fun getChoiceB(): String? {
        return mChoiceB
    }

    fun getChoiceC(): String? {
        return mChoiceC
    }

    fun getChoiceD(): String? {
        return mChoiceD
    }

    fun getChoiceE(): String? {
        return mChoiceE
    }

    fun getAnswer(): String? {
        return mAnswer
    }

    fun setUserChoice(choice : String){
        mUserChoice = choice
    }



}