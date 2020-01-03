package vnjp.monstarlaplifetime.apartmentssearch.screen.guests

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import vnjp.monstarlaplifetime.apartmentssearch.R
import java.util.*

@Suppress("DEPRECATION")
class GuestsFragmentBottomSheet : BottomSheetDialogFragment(), View.OnClickListener{
    private lateinit var imbPlusAdults: ImageButton
    private lateinit var imbPlusChildren: ImageButton
    private lateinit var imbPlusInfants: ImageButton
    private lateinit var imbMinusAdults: ImageButton
    private lateinit var imbMinusChildren: ImageButton
    private lateinit var imbMinusInfants: ImageButton
    private lateinit var tvAccountPeople: TextView
    private lateinit var tvAccountAdults: TextView
    private lateinit var tvAccountChildren: TextView
    private lateinit var tvAccountInfants: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_guests, container, false)
        initView(view)
        initEvent()
        return view.rootView
    }

    private fun initView(view: View?) {
        imbPlusAdults = view!!.findViewById(R.id.imbPlusAdults)
        imbMinusAdults = view.findViewById(R.id.imbMinusAdults)
        imbPlusChildren = view.findViewById(R.id.imbPlusChildren)
        imbMinusChildren = view.findViewById(R.id.imbMinusChildren)
        imbPlusInfants = view.findViewById(R.id.imbPlusInfants)
        imbMinusInfants = view.findViewById(R.id.imbMinusInfants)
        tvAccountPeople = view.findViewById(R.id.tvAccountPeople)
        tvAccountAdults = view.findViewById(R.id.tvAccountAdults)
        tvAccountChildren = view.findViewById(R.id.tvAccountChildren)
        tvAccountInfants = view.findViewById(R.id.tvAccountInfants)


    }

    private fun initEvent() {
        imbPlusAdults.setOnClickListener(this)
        imbMinusAdults.setOnClickListener(this)
        imbPlusChildren.setOnClickListener(this)
        imbMinusChildren.setOnClickListener(this)
        imbPlusInfants.setOnClickListener(this)
        imbMinusInfants.setOnClickListener(this)


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

    override fun onClick(v: View?) {
        var accountAdult: Int = tvAccountAdults.text.toString().toInt()
        var accountChildren: Int = tvAccountChildren.text.toString().toInt()
        var accountInfants: Int = tvAccountInfants.text.toString().toInt()
        var accountPeople: Int = tvAccountPeople.text.toString().toInt()
        when (v?.id) {
            R.id.imbPlusAdults -> {
                ++accountAdult
                ++accountPeople
                tvAccountAdults.setText(accountAdult.toString())
                tvAccountPeople.setText(accountPeople.toString())
                imbMinusAdults.setImageDrawable(
                    ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.ic_feather_minus_circle
                    )
                )
            }
            R.id.imbMinusAdults -> {
                --accountAdult
                --accountPeople
                if (accountAdult == 0) {
                    imbMinusAdults.setImageDrawable(
                        ContextCompat.getDrawable(
                            this.requireContext(),
                            R.drawable.ic_feather_minus_circle_hint
                        )
                    )
                    //imbMinusAdults.isClickable = false
                } else if (accountAdult < 0 && accountPeople < 0) {
                    accountAdult = 0
                    accountPeople = 0
                }
                tvAccountAdults.setText(accountAdult.toString())
                tvAccountPeople.setText(accountPeople.toString())
            }

            R.id.imbPlusChildren -> {
                ++accountChildren
                ++accountPeople
                tvAccountChildren.setText(accountChildren.toString())
                tvAccountPeople.setText(accountPeople.toString())
                imbMinusChildren.setImageDrawable(
                    ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.ic_feather_minus_circle
                    )
                )
            }
            R.id.imbMinusChildren -> {
                --accountChildren
                --accountPeople
                if (accountChildren == 0) {
                    imbMinusChildren.setImageDrawable(
                        ContextCompat.getDrawable(
                            this.requireContext(),
                            R.drawable.ic_feather_minus_circle_hint
                        )
                    )
                    //imbMinusChildren.isClickable = false
                } else if (accountChildren < 0 && accountPeople < 0) {
                    accountChildren = 0
                    accountPeople = 0
                }
                tvAccountChildren.setText(accountChildren.toString())
                tvAccountPeople.setText(accountPeople.toString())

            }
            R.id.imbPlusInfants -> {
                ++accountInfants
                ++accountPeople
                tvAccountInfants.setText(accountInfants.toString())
                tvAccountPeople.setText(accountPeople.toString())
                imbMinusInfants.setImageDrawable(
                    ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.ic_feather_minus_circle
                    )
                )
            }
            R.id.imbMinusInfants -> {
                --accountInfants
                --accountPeople
                if (accountInfants == 0) {
                    imbMinusInfants.setImageDrawable(
                        ContextCompat.getDrawable(
                            this.requireContext(),
                            R.drawable.ic_feather_minus_circle_hint
                        )
                    )
                    //imbMinusInfants.isClickable = false
                } else if (accountInfants < 0 && accountPeople < 0) {
                    accountInfants = 0
                    accountPeople = 0
                }
                tvAccountInfants.setText(accountInfants.toString())
                tvAccountPeople.setText(accountPeople.toString())
            }
        }
    }


}