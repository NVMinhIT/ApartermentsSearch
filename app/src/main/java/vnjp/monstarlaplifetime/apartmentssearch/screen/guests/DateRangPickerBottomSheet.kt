package vnjp.monstarlaplifetime.apartmentssearch.screen.guests

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet.*
import vnjp.monstarlaplifetime.apartmentssearch.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class DateRangPickerBottomSheet : BottomSheetDialogFragment() {

    //    private var calendarListeners: CalendarListener? = null
    private var calendarView: DateRangeCalendarView? = null

    fun newInstance(): DateRangPickerBottomSheet {
        return DateRangPickerBottomSheet()
    }

//    fun setCalendarListener(calendarListener: CalendarListener) {
//        this.calendarListeners = calendarListener
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)
        initView(view)
        initEvent()
        return view.rootView
    }

    private fun initView(view: View?) {
        calendarView = view!!.findViewById(R.id.calendar)
    }

    private fun initEvent() {
        calendarView?.setCalendarListener(object : DateRangeCalendarView.CalendarListener {
            override fun onDateRangeSelected(startDate: Calendar?, endDate: Calendar?) {

                val long: Long = endDate!!.timeInMillis - startDate!!.timeInMillis
                val longDay = TimeUnit.MILLISECONDS.toDays(long)
                tvNumberDay.setText(longDay.toString())
                Log.d("hihi", longDay.toString())
                val sdf = SimpleDateFormat("EEE, MMM dd", Locale.US)
                //  val date: Date
                val s = sdf.format(startDate.time)
                val e = sdf.format(endDate.time)
                textDateCheckIn.text = s.toString()
                textDateCheckOut.text = e.toString()

//                val e = sdf.format(endDate!!.time)
//                Toast.makeText(
//                    context,
//                    "Start Date: " + s.toString() + " End date: " + e.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()
                //calendarView!!.setSelectedDateRange(startDate, endDate);
            }

            override fun onFirstDateSelected(startDate: Calendar?) {
                tvNumberDay.setText("0")
//                Toast.makeText(
//                    context,
//                    "Start Date: " + s.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()
//                Toast.makeText(
//                    context,
//                    "Start Date: " + startDate?.getTime().toString(),
//                    Toast.LENGTH_SHORT
//                ).show();
            }

        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val d = super.onCreateDialog(savedInstanceState)
        d.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            var behaviour: BottomSheetBehavior<*>? = null
            if (bottomSheet != null) {
                behaviour = BottomSheetBehavior.from(bottomSheet)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
            behaviour?.setBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(
                    bottomSheet: View,
                    newState: Int
                ) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        //handleUserExit()
                        dismiss()
                    }
                }

                override fun onSlide(
                    bottomSheet: View,
                    slideOffset: Float
                ) {
                }
            })
        }
        return d
    }
}