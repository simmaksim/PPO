package com.example.lr2.viewModels

import android.app.Application
import android.content.res.Resources
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.lr2.data.TabataEntity
import com.example.lr2.R

class TimerViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var tabata: TabataEntity
    var currentText = MutableLiveData<String>("Warm-up")
    var prevText = MutableLiveData<String>("")
    var nextText = MutableLiveData<String>("Work")

    var timeRemainingText = MutableLiveData<String>("00:00")
    var timePercentRemaining: MutableLiveData<Int> = MutableLiveData(100)
    var isFinished = MutableLiveData<Boolean>(false)

    var countDownTimer: CountDownTimer? = null
    private var timeRemaining: Long = 0
    private var timeRemainingStatic = timeRemaining
    var currIndex = 0
    private var stagesCount = 0
    private val interval: Long = 1000
    var isRunning: Boolean = false
    lateinit var res: Resources
    var sequenceText = arrayListOf<String>()
    var sequenceTime = arrayListOf<Int>()

    private val attr = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
    private val soundPool = SoundPool.Builder().setAudioAttributes(attr).build()

    fun setTabata(tabata: TabataEntity) {
        this.tabata = tabata
        res = getApplication<Application>().resources
        res.updateConfiguration(res.configuration, res.displayMetrics)   // for language update
        setInitValues()
        soundPool.load(getApplication<Application>().applicationContext, R.raw.notification, 1)
        createSequence()
    }

    private fun setInitValues() {
        stagesCount = tabata.cycles * (tabata.repeats * 2 + 1)
        timeRemaining = (tabata.warm_up * 1000).toLong()
        timeRemainingStatic = timeRemaining
        timeRemainingText.value = EditTabataViewModel.getTime(tabata.warm_up)
        currentText.value = res.getString(R.string.warm_up)
        nextText.value = res.getString(R.string.work_short)
    }

    private fun createSequence() {
        for (i in 1..tabata.cycles) {
            if (i == 1) {
                sequenceText.add(res.getString(R.string.warm_up))
                sequenceTime.add(tabata.warm_up)
            }
            else {
                sequenceText.add(res.getString(R.string.cooldown))
                sequenceTime.add(tabata.cooldown)
            }
            for (j in 1..tabata.repeats) {
                sequenceText.add(res.getString(R.string.work_short))
                sequenceText.add(res.getString(R.string.rest))
                sequenceTime.add(tabata.work)
                sequenceTime.add(tabata.rest)
            }
        }
    }

    fun pause() {
        isRunning = false
        countDownTimer?.cancel()
    }

    private fun getTimeRemainingText(time: Long) = EditTabataViewModel.getTime(time.toInt() / 1000)

    fun startTimer() {
        isRunning = true
        countDownTimer = object : CountDownTimer(timeRemaining, interval) {
            override fun onFinish() {
                soundPool.play(1, 1F, 1F, 1, 0, 1F)
                currIndex += 1
                if (currIndex < 0) currIndex = 0
                timePercentRemaining.value = 100
                if (currIndex == stagesCount) {
                    currentText.value = res.getString(R.string.complete)
                    prevText.value = res.getString(R.string.timerCompleted)
                    isFinished.value = true
                } else {
                    currentText.value = sequenceText[currIndex]
                    nextText.value = if (currIndex == stagesCount - 1) ""
                                     else sequenceText[currIndex+1]
                    prevText.value = if (currIndex == 0) "" else sequenceText[currIndex-1]
                    timeRemaining = sequenceTime[currIndex].toLong() * 1000
                    timeRemainingStatic = timeRemaining
                    timeRemainingText.value = getTimeRemainingText(timeRemaining)
                    startTimer()
                }
            }
            override fun onTick(millisUntilFinished: Long) {
                timeRemainingText.value = getTimeRemainingText(timeRemaining)
                timePercentRemaining.value = ((timeRemaining * 100) / timeRemainingStatic).toInt()
                timeRemaining -= interval
            }
        }.start()
    }

    fun rewind(i: Int) {
        currIndex -= i
        countDownTimer!!.cancel()
        countDownTimer!!.onFinish()
    }
}