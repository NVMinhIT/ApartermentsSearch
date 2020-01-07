package vnjp.monstarlaplifetime.apartmentssearch.screen.guests

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet.*
import org.greenrobot.eventbus.EventBus
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.Commons
import java.util.*
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class DateRangPickerBottomSheet : BottomSheetDialogFragment() {


    private var calendarView: DateRangeCalendarView? = null
    private var dDay: String? = null
    private lateinit var textApply: TextView

    fun newInstance(): DateRangPickerBottomSheet {
        return DateRangPickerBottomSheet()
    }

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
        textApply = view.findViewById(R.id.textApply)
    }

    private fun initEvent() {
        calendarView?.setCalendarListener(object : DateRangeCalendarView.CalendarListener {
            override fun onDateRangeSelected(startDate: Calendar?, endDate: Calendar?) {
                val long: Long = endDate!!.timeInMillis - startDate!!.timeInMillis
                val longDay = TimeUnit.MILLISECONDS.toDays(long)
                tvNumberDay.setText(longDay.toString())
                val sDay = Commons.getStringCurrentDateTime(startDate.time)
                val eDay = Commons.getStringCurrentDateTime(endDate.time)
                textDateCheckIn.text = sDay.toString()
                textDateCheckOut.text = eDay.toString()
                val dCheckIn = Commons.getCurrentDateTime(startDate.time)
                val dCheckOut = Commons.getCurrentDateTime(endDate.time)
                val day = StringBuffer()
                day.append(dCheckIn).append(" - ").append(dCheckOut)
                dDay = day.toString()
                Log.d("HIHI", "${dDay}")

            }

            override fun onFirstDateSelected(startDate: Calendar?) {
                tvNumberDay.setText("0")

            }

        })
        // option day
        textApply.setOnClickListener {
            EventBus.getDefault().post(dDay)
            dialog?.dismiss()

        }
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