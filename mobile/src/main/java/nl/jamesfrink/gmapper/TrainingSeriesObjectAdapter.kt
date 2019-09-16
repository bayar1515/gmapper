package nl.jamesfrink.gmapper

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class TrainingSeriesObjectAdapter( /*private val stateManager : nl.jamesfrink.gmapper.StateManager,*/  val parentArrayList : ArrayListAdapter<TrainingSeriesObject>) : RecyclerView.Adapter< TrainingSeriesObjectAdapter.CustomViewHolder >(  ) {

    private var handler = Handler(  )

    override fun getItemCount(  ) : Int {
        return parentArrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from( parent?.context )
        val inflatedGestureRow = layoutInflater.inflate( R.layout.training_object_list_element, parent, false )
        return CustomViewHolder( inflatedGestureRow )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val trainingSeriesObject = parentArrayList[ position ]
        holder.bindTrainingSeriesObject( trainingSeriesObject )
        holder.tNumTimesFound?.addTextChangedListener(object :TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    inner class CustomViewHolder( val view : View ) : androidx.recyclerview.widget.RecyclerView.ViewHolder( view ) {
        lateinit var trainingSeriesObject: TrainingSeriesObject
        var tTimeStamp : TextView? = null
        var tLengthOfRecording: TextView? = null
        var tNumTimesFound: TextView? = null
        val colorAnimation : ValueAnimator = ValueAnimator.ofObject( ArgbEvaluator(  ), Color.argb( 255, 100, 255, 100 ), Color.argb( 0, 255, 255, 255 ) )

        init {
            tTimeStamp = view.findViewById< TextView >( R.id.textView_Date )
            tLengthOfRecording = view.findViewById< TextView >( R.id.textView_length )
            tNumTimesFound = view.findViewById< TextView >( R.id.textView_numRecognized )
            colorAnimation.duration = 1250 // milliseconds
            colorAnimation.addUpdateListener { animator ->
                view.setBackgroundColor( animator.animatedValue as Int )
            }
        }

        fun bindTrainingSeriesObject ( trainingSeriesObject: TrainingSeriesObject)
        {
            this.trainingSeriesObject = trainingSeriesObject
            tLengthOfRecording!!.text = trainingSeriesObject.gestureTimeLength
            tTimeStamp!!.text = trainingSeriesObject.timeStamp
            tNumTimesFound!!.text = trainingSeriesObject.numTimesRecognized.toString(  )
            trainingSeriesObject.numTimesRecognizedListener = { numTimesRecognized : Int ->
                handler.post( object : Runnable {
                    override fun run() {
                        run {
                            tNumTimesFound!!.text = numTimesRecognized.toString(  )
                            colorAnimation.start(  )
                        }
                    }
                } )
            }
        }
    }
}