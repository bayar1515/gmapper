package nl.jamesfrink.gmapper


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color

/**
 * Created by james on 20/01/2018.
 */
class GestureAdapter(private val stateManager : StateManager, private val parentArrayList : ArrayListAdapter<Gesture>) : androidx.recyclerview.widget.RecyclerView.Adapter< GestureAdapter.CustomViewHolder >(  ) {
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val gesture = parentArrayList[ position ]
        holder!!.bindGesture( gesture )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from( parent?.context )
        val inflatedGestureRow = layoutInflater.inflate( R.layout.gesture_list_element, parent, false )
        return CustomViewHolder( inflatedGestureRow )
    }


    // numberOfItems
    override fun getItemCount(  ) : Int {
        return parentArrayList.size
    }
    inner class CustomViewHolder( val view : View ) : androidx.recyclerview.widget.RecyclerView.ViewHolder( view ) {
        lateinit var gesture : Gesture
        var gestureName : TextView? = null
        var gestureActive : Switch? = null
        var gestureExamples : TextView? = null
        val colorAnimation : ValueAnimator = ValueAnimator.ofObject( ArgbEvaluator(  ), Color.argb( 255, 100, 255, 100 ), Color.argb( 0, 255, 255, 255 ) )

        init {
            //Log.w("CustViewHolder","ini")
            gestureName = view.findViewById< TextView >( R.id.textInput_gestureName)
            gestureActive = view.findViewById< Switch >( R.id.switch_gestureActive)
            gestureExamples = view.findViewById< TextView >( R.id.textView_gestureExamples)
            colorAnimation.duration = 1250 // milliseconds
            colorAnimation.addUpdateListener( { animator ->
                view.setBackgroundColor( animator.animatedValue as Int )
            } )

            gestureActive!!.setOnClickListener {
                // TODO: possibly init a cpp lib that externs so this doesn't have to call through statemanager
                stateManager.setGestureActive( adapterPosition, gestureActive!!.isChecked )
            }

            // Handle item click and set the selection

            view.findViewById<ConstraintLayout>( R.id.backGround ).setOnClickListener {
                stateManager.pagerAdapter.gestureFrame.displayFragment( 1 )
                stateManager.pagerAdapter.gestureFrame.gestureEditor.setNewGesture( gesture, adapterPosition )
            }


        }

        fun bindGesture ( g: Gesture)
        {
            this.gesture = g
            gestureName!!.text = gesture.nameOfGesture
            gestureActive!!.isChecked = gesture.active
            gestureExamples!!.text = gesture.numberOfRecordings.toString(  )

            gesture.recognizedCallback = {
                colorAnimation.start(  )
            }
        }
    }
}

