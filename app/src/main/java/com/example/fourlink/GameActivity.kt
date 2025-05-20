package com.example.fourlink

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setMargins
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import com.example.fourlink.GameEndDialogFragment

class GameActivity : TransitionActivity() {
    private val ROWS = 6
    private val COLS = 7
    private val board = Array(ROWS) { IntArray(COLS) { -1 } }
    private lateinit var cellViews: Array<Array<ImageView>>
    var isGameEnded = false

    private var playerTurn = 0
        set(value) {
            field = value % 2
        }

    override fun onBackPressed() {
        if (isGameEnded) {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_screen)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        // this disables the board layout view kay mao lang bati ang statically added
        val gameBoardImage = findViewById<ImageView>(R.id.game_board)
        val back_button = findViewById<ImageView>(R.id.back_button)
        val play_again_button = findViewById<Button>(R.id.play_again_button)

        gameBoardImage.visibility = View.INVISIBLE
        back_button.visibility = View.GONE
        play_again_button.visibility = View.GONE

        // val testTurn_Button = findViewById<Button>(R.id.change_turn_button)

        val surrender_button = findViewById<Button>(R.id.surrender_button)
        val yellow_player = findViewById<TextView>(R.id.yellow_player_tab)
        val red_player = findViewById<TextView>(R.id.red_player_tab)

        val button1 = findViewById<Button>(R.id.col_button_1)
        val button2 = findViewById<Button>(R.id.col_button_2)
        val button3 = findViewById<Button>(R.id.col_button_3)
        val button4 = findViewById<Button>(R.id.col_button_4)
        val button5 = findViewById<Button>(R.id.col_button_5)
        val button6 = findViewById<Button>(R.id.col_button_6)
        val button7 = findViewById<Button>(R.id.col_button_7)

        addBoardBackground()
        initializeGrid()
        updatePlayerUI()

        button1.setOnClickListener { dropDisc(0) }
        button2.setOnClickListener { dropDisc(1) }
        button3.setOnClickListener { dropDisc(2) }
        button4.setOnClickListener { dropDisc(3) }
        button5.setOnClickListener { dropDisc(4) }
        button6.setOnClickListener { dropDisc(5) }
        button7.setOnClickListener { dropDisc(6) }

        surrender_button.setOnClickListener {
            if (isGameEnded) return@setOnClickListener

            val currentPlayer = if (playerTurn == 0) "YELLOW" else "RED"
            val dialog = SurrenderDialogFragment.newInstance(currentPlayer)

            dialog.setOnSurrenderConfirmed { winner ->
                isGameEnded = true
                gameEnd("PLAYER $winner WINS")
            }

            dialog.show(fragmentManager, "SurrenderDialog")
        }

        back_button.setOnClickListener{
            finish()
        }

        play_again_button.setOnClickListener{
            startActivity(intent)
            finish()
        }

        /*back_button.setOnClickListener {
            startActivity(Intent(this, MainMenuActivity::class.java))
        }*/

        /*testTurn_Button.setOnClickListener {
            playerTurn++
            updatePlayerUI()
        }*/
    }

    private fun addBoardBackground() {
        val rootLayout = findViewById<ConstraintLayout>(R.id.root_layout)
        val boardImage = ImageView(this).apply {
            id = ImageView.generateViewId()
            setImageResource(R.drawable.board_full_blue)
            scaleType = ImageView.ScaleType.FIT_XY
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            ).apply {
                topToTop = R.id.game_grid
                bottomToBottom = R.id.game_grid
                startToStart = R.id.game_grid
                endToEnd = R.id.game_grid

                topMargin = -25
                bottomMargin = -50
            }
        }
        rootLayout.addView(boardImage, 0) //to add it behind everything
    }

    private fun initializeGrid() {
        val gridLayout = findViewById<GridLayout>(R.id.game_grid)
        gridLayout.removeAllViews()
        gridLayout.rowCount = ROWS
        gridLayout.columnCount = COLS

        cellViews = Array(ROWS) { row ->
            Array(COLS) { col ->
                val cell = ImageView(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                        setMargins(4)
                    }
                    setImageResource(R.drawable.game_piece_empty)
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                }
                gridLayout.addView(cell)
                cell
            }
        }
    }

    private fun dropDisc(column: Int) {
        if (isGameEnded) return

        val start_text = findViewById<TextView>(R.id.start_text)
        start_text.visibility = View.GONE

        for (row in ROWS - 1 downTo 0) {
            if (board[row][column] == -1) {
                board[row][column] = playerTurn
                addDiscToGrid(row, column, playerTurn)

                val winningChips = checkWin(row, column)

                if (winningChips.isNotEmpty()) {
                    val winner = if (playerTurn == 0) "YELLOW" else "RED"
                    // Toast.makeText(this, "PLAYER $winner WINS!", Toast.LENGTH_LONG).show()
                    isGameEnded = true

                    // do something if someone wins
                    blinkWinningChips(winningChips)

                    android.os.Handler().postDelayed({gameEnd("PLAYER $winner WINS")}, 2000)

                } else if (isBoardFull()){
                    // Toast.makeText(this, "GAME DRAW", Toast.LENGTH_LONG).show()
                    isGameEnded = true

                    // do something if it's a draw
                    gameEnd("Game Draw :(")

                } else {
                    playerTurn++
                    updatePlayerUI()
                }
                return
            }
        }
        // toast to check
        // Toast.makeText(this, "Column is full!", Toast.LENGTH_SHORT).show()
    }

    private fun addDiscToGrid(row: Int, col: Int, player: Int) {  // for dropping animation
        val discRes = if (player == 0) R.drawable.game_piece_yellow else R.drawable.game_piece_red
        val imageView = cellViews[row][col]

        imageView.setImageResource(discRes)
        imageView.translationY = -1000f

        imageView.animate().translationY(0f).setDuration(400).setInterpolator(android.view.animation.BounceInterpolator()).start()
    }

    private fun checkWin(row: Int, col: Int): List<Pair<Int, Int>> {
        val player = board[row][col]
        val directions = listOf(
            Pair(1, 0),   // horizontal
            Pair(0, 1),   // vertical
            Pair(1, 1),   // diagonal /
            Pair(1, -1)   // diagonal \
        )
        // lala ani oi

        for ((dx, dy) in directions) {
            val line = mutableListOf<Pair<Int, Int>>()
            line.add(Pair(row, col))

            // Forward
            var r = row + dy
            var c = col + dx
            while (r in 0 until ROWS && c in 0 until COLS && board[r][c] == player) {
                line.add(Pair(r, c))
                r += dy
                c += dx
            }

            // Backward
            r = row - dy
            c = col - dx
            while (r in 0 until ROWS && c in 0 until COLS && board[r][c] == player) {
                line.add(Pair(r, c))
                r -= dy
                c -= dx
            }

            if (line.size >= 4) return line
        }
        return emptyList()
    }


    private fun isBoardFull(): Boolean {
        for (row in board) {
            for (cell in row) {
                if (cell == -1) return false
            }
        }
        return true
    }

    private fun updatePlayerUI() {
        val yellow_player = findViewById<TextView>(R.id.yellow_player_tab)
        val red_player = findViewById<TextView>(R.id.red_player_tab)
        if (playerTurn == 0) { // true = yellow, false = red
            yellow_player.setBackgroundResource(R.drawable.player_yellow_turn)
            red_player.setBackgroundResource(R.drawable.player_red)
            yellow_player.text = "Yellow's Turn"
            red_player.text = ""
        } else {
            yellow_player.setBackgroundResource(R.drawable.player_yellow)
            red_player.setBackgroundResource(R.drawable.player_red_turn)
            red_player.text = "Red's Turn"
            yellow_player.text = ""
        }
    }

    private fun gameEnd(message: String){
        val dialog = GameEndDialogFragment.newInstance(message)
        val back_button = findViewById<ImageView>(R.id.back_button)
        val play_again_button = findViewById<Button>(R.id.play_again_button)
        dialog.setCallbacks(
            onRestart = {
                startActivity(intent)
                finish()
            },
            onMainMenu = {
                startActivity(Intent(this, MainMenuActivity::class.java))
                finish()
            }
        )
        dialog.show(fragmentManager, "GameEndDialog")
        back_button.visibility = View.VISIBLE
        play_again_button.visibility = View.VISIBLE
    }

    private fun blinkWinningChips(positions: List<Pair<Int, Int>>) {
        val handler = android.os.Handler()
        var isVisible = true
        val playerDisc = if (playerTurn == 0) R.drawable.game_piece_yellow else R.drawable.game_piece_red

        val blinkRunnable = object : Runnable {
            override fun run() {
                for ((r, c) in positions) {
                    cellViews[r][c].setImageResource(
                        if (isVisible) R.drawable.game_piece_empty else playerDisc
                    )
                }
                isVisible = !isVisible
                handler.postDelayed(this, 400) // change every 400ms
            }
        }
        handler.post(blinkRunnable) // start blinking them winning chips
    }


}

