package com.transformers.test.screens.transformers

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.transformers.test.R
import com.transformers.test.models.transformers.Transformer
import com.transformers.test.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_battle.*

class BattleFragment : BaseFragment() {

    lateinit var mRecyclerViewAdapter: BattleAdapter

    override fun getLayoutId() = R.layout.fragment_battle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = arguments?.getSerializable("transformers") as? ArrayList<Transformer>
        performFight(list)
    }

    private fun performFight(list: ArrayList<Transformer>?) {
        var descepticons = list?.filter { it.team == "D" }
        var autobots = list?.filter { it.team == "A" }

        descepticons = descepticons?.sortedByDescending { it.rank }
        autobots = autobots?.sortedByDescending { it.rank }

        var battles = 0
        var autobotWins = 0
        var descepticonsWins = 0
        var result: Pair<Transformer, Transformer>?
        val results = arrayListOf<Pair<Transformer, Transformer>>()
        while ((battles < (autobots?.size ?: 0) && (battles < (descepticons?.size ?: 0)))) {
            val autobot = autobots?.get(battles)
            val descepticon = descepticons?.get(battles)
            result = autobot?.let { descepticon?.let { it1 -> fight(it, it1) } }
            if (result != null) {
                if (result.first.team == "A") autobotWins++ else descepticonsWins++
                results.add(result)
            } else {
                results.add(Pair(autobot!!, descepticon!!))
            }
            battles++
        }

        mRecyclerViewAdapter = BattleAdapter(requireContext(), results)
        battleList.adapter = mRecyclerViewAdapter
        battleList.layoutManager = LinearLayoutManager(requireActivity())

        winnerText.text =
            "${winnerText.text}\n${if (autobotWins == descepticonsWins) "Its a Tie" else if (autobotWins > descepticonsWins) "Winner is AUTOBOTS" else "Winner is DESCEPTICONS"}\n"
        winnerText.text =
            "${winnerText.text}\nSurvivors\n"
        autobots?.forEachIndexed { index, transformer ->
            if (index >= battles) winnerText.text =
                "${winnerText.text}${transformer.name} of team ${transformer.getTeamFormatted()}\n"
        }
        descepticons?.forEachIndexed { index, transformer ->
            if (index >= battles) winnerText.text =
                "${winnerText.text}${transformer.name} of team ${transformer.getTeamFormatted()}\n"
        }
    }

    private fun calculateOverall(transformer: Transformer): Int {
        return transformer.strength + transformer.intelligence + transformer.speed + transformer.endurance + transformer.firepower
    }

    private fun fight(
        autobot: Transformer,
        descepticon: Transformer
    ): Pair<Transformer, Transformer>? {
        return when {
            autobot.name.toLowerCase() == "optimus prime" && descepticon.name.toLowerCase() == "predaking" -> null
            autobot.name.toLowerCase() == "optimus prime" && descepticon.name.toLowerCase() != "predaking" -> {
                autobot.winner = true
                Pair(autobot, descepticon)
            }
            autobot.name.toLowerCase() != "optimus prime" && descepticon.name.toLowerCase() == "predaking" -> {
                descepticon.winner = true
                Pair(descepticon, autobot)
            }
            autobot.name.toLowerCase() == "optimus prime" && descepticon.name.toLowerCase() == "optimus prime" -> null
            autobot.name.toLowerCase() == "predaking" && descepticon.name.toLowerCase() == "predaking" -> null
            autobot.courage <= descepticon.courage - 4 && autobot.strength <= descepticon.strength - 3 -> {
                descepticon.winner = true
                Pair(descepticon, autobot)
            }
            descepticon.courage <= autobot.courage - 4 && descepticon.strength <= autobot.strength - 3 -> {
                autobot.winner = true
                Pair(autobot, descepticon)
            }
            autobot.skill - 3 >= descepticon.skill -> {
                autobot.winner = true
                Pair(autobot, descepticon)
            }
            descepticon.skill - 3 >= autobot.skill -> {
                descepticon.winner = true
                Pair(descepticon, autobot)
            }
            calculateOverall(autobot) < calculateOverall(descepticon) -> {
                descepticon.winner = true
                Pair(descepticon, autobot)
            }
            calculateOverall(autobot) > calculateOverall(descepticon) -> {
                autobot.winner = true
                Pair(autobot, descepticon)
            }
            else -> null
        }
    }
}