package com.example.medstime.ui.medication

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.domain.models.MedicationIntakeModel
import com.example.medstime.R
import com.example.medstime.databinding.FragmentMedicationBinding
import com.example.medstime.ui.medication.adapters.MedicationListPagerAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.Scanner
import java.util.concurrent.TimeUnit


class MedicationFragment : Fragment(R.layout.fragment_medication) {

    private var _binding: FragmentMedicationBinding? = null
    private val binding get() = _binding!!
    private val currentDate: MedicationIntakeModel.Date
    private var dateList: List<MedicationIntakeModel.Date>

    init {
        currentDate = getDate()
        dateList = generateDateList(currentDate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicationBinding.bind(view)

        initView()
    }

    private fun initView() {
        with(binding) {
            calendar.visibility = View.GONE//todo баг calendarView
            setPagerAdapter(currentDate)
            showCalendarText.setFactory {
                TextView(requireContext()).apply {
                    textSize = 15f
                    gravity = Gravity.CENTER_HORIZONTAL
                    typeface = resources.getFont(R.font.roboto_regular)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.meds_time_primary_dark
                        )
                    )
                }
            }
            showCalendarText.setText(getDisplayDate(currentDate))
            showCalendar.post { setTopMargin(binding.showCalendar.height) }
            showCalendar.setOnClickListener { changeVisible(false) }
            hideCalendar.setOnClickListener { changeVisible(true) }
            calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val date = MedicationIntakeModel.Date(dayOfMonth, month + 1, year)
                setPagerAdapter(date)
                showCalendarText.setText(getDisplayDate(date))
                hideCalendar.callOnClick()
            }
            val maxDate = TimeUnit.DAYS.toMillis(MAX_NUMBER_DAYS)
            calendar.maxDate = (Date().time + maxDate)
            addNewMedication.setOnClickListener {
                val navHostFragment =
                    requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                navHostFragment.navController.navigate(R.id.addMedFragment)
            }
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val displayDate = getDisplayDate(dateList[position])
                    showCalendarText.setText(displayDate)
                }
            })
        }
    }


    private fun setPagerAdapter(date: MedicationIntakeModel.Date) {
        dateList = generateDateList(date)
        binding.viewPager.adapter = MedicationListPagerAdapter(this, dateList)
        binding.viewPager.setCurrentItem(MIN_NUMBER_DAYS.toInt(), false)
    }

    /**Метод **generateDateList** генерирует список дат для адаптера ViewPager2**/
    private fun generateDateList(date: MedicationIntakeModel.Date): List<MedicationIntakeModel.Date> {
        return LocalDate.of(date.year, date.month, date.day).run {
            val startDate = this.minusDays(MIN_NUMBER_DAYS)
            val endDate = this.plusDays(MAX_NUMBER_DAYS)
            val dateList = mutableListOf<MedicationIntakeModel.Date>()
            var currentDateIterator = startDate
            while (currentDateIterator.isBefore(endDate) || currentDateIterator.isEqual(endDate)) {
                dateList.add(
                    MedicationIntakeModel.Date(
                        day = currentDateIterator.dayOfMonth,
                        month = currentDateIterator.monthValue,
                        year = currentDateIterator.year
                    )
                )
                currentDateIterator = currentDateIterator.plusDays(1)
            }
            dateList
        }
    }

    private fun getDisplayDate(date: MedicationIntakeModel.Date): String {
        val formatter = DateTimeFormatter.ofPattern(
            "d MMMM", Locale(
                "ru", "RU"
            )
        )
        return LocalDate.of(date.year, date.month, date.day).format(formatter)
    }

    private fun getDate(): MedicationIntakeModel.Date {
        val formatterIntakeDate = DateTimeFormatter.ofPattern("d M yyyy")
        val scanner = Scanner(LocalDateTime.now().format(formatterIntakeDate))
        return MedicationIntakeModel.Date(scanner.nextInt(), scanner.nextInt(), scanner.nextInt())
    }

    private fun changeVisible(calendarIsVisible: Boolean) {
        with(binding) {
            if (calendarIsVisible) {
                calendar.visibility = View.GONE
                showCalendarLayout.visibility = View.VISIBLE
                hideCalendar.visibility = View.GONE
            } else {
                calendar.visibility = View.VISIBLE
                showCalendarLayout.visibility = View.GONE
                hideCalendar.visibility = View.VISIBLE
            }
        }
    }

    private fun setTopMargin(topMargin: Int) {
        val layoutParams = binding.viewPager.layoutParams as FrameLayout.LayoutParams
        layoutParams.setMargins(0, topMargin, 0, 0)
        binding.viewPager.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        const val MAX_NUMBER_DAYS: Long = 14
        const val MIN_NUMBER_DAYS: Long = 14
    }
}