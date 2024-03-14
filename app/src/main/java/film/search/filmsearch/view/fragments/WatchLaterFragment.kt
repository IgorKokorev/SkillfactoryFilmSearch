package film.search.filmsearch.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import film.search.filmsearch.data.entity.WatchLaterFilm
import film.search.filmsearch.databinding.FragmentWatchLaterBinding
import film.search.filmsearch.utils.AlarmService
import film.search.filmsearch.utils.AnimationHelper
import film.search.filmsearch.utils.AutoDisposable
import film.search.filmsearch.utils.addTo
import film.search.filmsearch.view.rvadapters.TopSpacingItemDecoration
import film.search.filmsearch.view.rvadapters.WatchLaterFilmRecyclerAdapter
import film.search.filmsearch.viewmodel.WatchLaterFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Calendar

// 'watch later' fragment
class WatchLaterFragment : Fragment() {
    private lateinit var binding: FragmentWatchLaterBinding
    private lateinit var filmsAdapter: WatchLaterFilmRecyclerAdapter
    private val viewModel: WatchLaterFragmentViewModel by viewModels<WatchLaterFragmentViewModel>()
    private val autoDisposable = AutoDisposable()
    private var filmsDataBase = listOf<WatchLaterFilm>()
        set(value) {
            if (field == value) return
            field = value
            filmsAdapter.addItems(field)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoDisposable.bindTo(lifecycle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchLaterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.watchLaterFilmList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list -> filmsDataBase = list }
            .addTo(autoDisposable)

        initRecycler()
        AnimationHelper.performFragmentCircularRevealAnimation(binding.root, requireActivity(), 2)
    }

    private fun initRecycler() {

        filmsAdapter = WatchLaterFilmRecyclerAdapter(
            object : WatchLaterFilmRecyclerAdapter.OnWatchLaterItemClickListener {
                override fun click(
                    watchLaterFilm: WatchLaterFilm
                ) {
                    setNewTime(watchLaterFilm)
                }

                override fun deleteClick(watchLaterFilm: WatchLaterFilm) {
                    AlarmService(binding.root.context).deleteAlarm(watchLaterFilm)
                    viewModel.interactor.deleteFilmFromWatchLater(watchLaterFilm)
                }
            })

        filmsAdapter.addItems(filmsDataBase)
        binding.watchLaterRecycler.adapter = filmsAdapter
        val decorator = TopSpacingItemDecoration(8)
        binding.watchLaterRecycler.addItemDecoration(decorator)
    }

    private fun setNewTime(watchLaterFilm: WatchLaterFilm) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = watchLaterFilm.time

        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        DatePickerDialog(
            binding.root.context,
            { _, dpdYear, dpdMonth, dayOfMonth ->
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, pickerMinute ->
                        val pickedDateTime = Calendar.getInstance()
                        pickedDateTime.set(
                            dpdYear,
                            dpdMonth,
                            dayOfMonth,
                            hourOfDay,
                            pickerMinute,
                            0
                        )
                        val dateTimeInMillis = pickedDateTime.timeInMillis
                        if (dateTimeInMillis < System.currentTimeMillis()) {
                            println("!!! ALARM SERVICE !!! Current time in millis: ${System.currentTimeMillis()} is after the time to set Watch Later Alarm: ${dateTimeInMillis}")
                            return@OnTimeSetListener
                        }
                        // saving film and alarm time to db
                        watchLaterFilm.time = dateTimeInMillis
                        AlarmService(binding.root.context).editAlarm(watchLaterFilm)
                        viewModel.interactor.saveWatchLater(watchLaterFilm)

                    }

                TimePickerDialog(
                    context,
                    timeSetListener,
                    currentHour,
                    currentMinute,
                    true
                ).show()

            },
            currentYear,
            currentMonth,
            currentDay
        ).show()
    }
}
