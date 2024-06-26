package com.example.hopeshipstrive

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.hopeshipstrive.databinding.FragmentAlertDialogBinding
import com.example.hopeshipstrive.util.getIntOrNull

class AlertDialogFragment : DialogFragment() {

    private var _binding: FragmentAlertDialogBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var titleText: String? = null
    private var titleColor: Int? = null
    private var titleAlignment: Int? = null
    private var titleFont: Int? = null

    private var subtitleText: String? = null
    private var subtitleColor: Int? = null
    private var subtitleAlignment: Int? = null
    private var subtitleFont: Int? = null

    private var button1Text: String? = null
    private var button2Text: String? = null
    private var button3Text: String? = null
    private var button4Text: String? = null
    private var button5Text: String? = null
    private var button6Text: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertDialogBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        binding.closeButton.setOnClickListener { dismiss() }

        readArgs()

        setTextViewParameters(
            binding.titleTextView,
            titleText,
            titleColor,
            titleAlignment,
            titleFont
        )

        setTextViewParameters(
            binding.subtitleTextView,
            subtitleText,
            subtitleColor,
            subtitleAlignment,
            subtitleFont
        )

        (activity as? Listener)?.let { listener ->
            setButtonParameters(binding.button1, button1Text, listener::onButton1Click)
            setButtonParameters(binding.button2, button2Text, listener::onButton2Click)
            setButtonParameters(binding.button3, button3Text, listener::onButton3Click)
            setButtonParameters(binding.button4, button4Text, listener::onButton4Click)
            setButtonParameters(binding.button5, button5Text, listener::onButton5Click)
            setButtonParameters(binding.button6, button6Text, listener::onButton6Click)
        }
    }

    override fun onStart() {
        super.onStart()
        // Fixes issue where dialog renders too narrow to show content
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun readArgs() {
        arguments?.let {
            titleText = it.getString(KEY_TITLE_TEXT)
            titleColor = it.getIntOrNull(KEY_TITLE_COLOR)
            titleAlignment = it.getIntOrNull(KEY_TITLE_ALIGNMENT)
            titleFont = it.getIntOrNull(KEY_TITLE_FONT)

            subtitleText = it.getString(KEY_SUBTITLE_TEXT)
            subtitleColor = it.getIntOrNull(KEY_SUBTITLE_COLOR)
            subtitleAlignment = it.getIntOrNull(KEY_SUBTITLE_ALIGNMENT)
            subtitleFont = it.getIntOrNull(KEY_SUBTITLE_FONT)

            button1Text = it.getString(KEY_BUTTON_1_TEXT)
            button2Text = it.getString(KEY_BUTTON_2_TEXT)
            button3Text = it.getString(KEY_BUTTON_3_TEXT)
            button4Text = it.getString(KEY_BUTTON_4_TEXT)
            button5Text = it.getString(KEY_BUTTON_5_TEXT)
            button6Text = it.getString(KEY_BUTTON_6_TEXT)
        }
    }

    /**
     * Update text and other attributes for dialog title or subtitle. Immediately
     * returns unless [text] is non-null.
     *
     * Default values are set in fragment_alert_dialog.xml.
     *
     * @param textView The TextView to update.
     * @param text The new text for the TextView.
     * @param color The color resource ID for the text color.
     * @param alignment The gravity alignment for the text, such as [Gravity.START]
     */
    private fun setTextViewParameters(
        textView: TextView,
        text: String?,
        @ColorRes color: Int?,
        alignment: Int?,
        @FontRes font: Int?,
    ) {
        text?.let { txt ->
            textView.apply {
                setText(txt)
                color?.let { setTextColor(ContextCompat.getColor(context, it)) }
                alignment?.let { gravity = it }
                font?.let { typeface = resources.getFont(it) }
            }
        }
    }

    /**
     * Update text and other attributes for dialog button. Also makes button visible,
     * but immediately returns unless [text] is non-null.
     *
     * @param button The Button to update.
     * @param text The new text for the Button.
     * @param onClick The click listener for the Button.
     */
    private fun setButtonParameters(
        button: Button,
        text: String?,
        onClick: () -> Unit,
    ) {
        text?.let { txt ->
            button.apply {
                setText(txt)
                visibility = View.VISIBLE
                setOnClickListener { onClick() }
            }
        }
    }

    companion object {
        private const val KEY_TITLE_TEXT = "title_text"
        private const val KEY_TITLE_COLOR = "title_color"
        private const val KEY_TITLE_ALIGNMENT = "title_alignment"
        private const val KEY_TITLE_FONT = "title_font"

        private const val KEY_SUBTITLE_TEXT = "subtitle_text"
        private const val KEY_SUBTITLE_COLOR = "subtitle_color"
        private const val KEY_SUBTITLE_ALIGNMENT = "subtitle_alignment"
        private const val KEY_SUBTITLE_FONT = "subtitle_font"

        private const val KEY_BUTTON_1_TEXT = "button1_text"
        private const val KEY_BUTTON_2_TEXT = "button2_text"
        private const val KEY_BUTTON_3_TEXT = "button3_text"
        private const val KEY_BUTTON_4_TEXT = "button4_text"
        private const val KEY_BUTTON_5_TEXT = "button5_text"
        private const val KEY_BUTTON_6_TEXT = "button6_text"

        /**
         * Creates a new instance of AlertDialogFragment, with UI parameters set.
         *
         * From project requirements: "0->N buttons, with distinct primary/secondary button
         * styling, and configurable on-click actions, etc. (note: a realistic limit for N would
         * be the max # buttons that can fit within 80% of the available vertical space in the
         * window. No need to support scrolling for this use-case)."
         * In a production environment, I would have a conversation with the PM or designer about
         * this requirement. A simple dialog should not have so many buttons. Also, Android already
         * has built-in dialogs to use, following Material Design standards. If, for some reason,
         * there was a necessity for a special dialog (fragment overlay) showing many buttons,
         * a custom Fragment could be made for it - preferably not a DialogFragment.
         * The 0->N requirement is particularly problematic. "80% of the available vertical space"
         * would vary widely depending on the model of phone, if it is in portrait or landscape, and
         * if split screen mode is in use. Assuming there will always be an absolute minimum amount
         * of vertical space is risky.
         *
         * We could programmatically determine the amount of vertical space available, and only show
         * enough buttons to fit. But that would be complicated and pointless, as then we would
         * be discarding buttons the app wants to show. Better to have a fixed limit so only
         * that many buttons can be passed to `AlertDialogFragment`. 6 buttons seems reasonable;
         * that's a lot of buttons, but still a fixed amount, and takes up most of the screen height
         * on even the tallest phones.
         *
         * Given the requirements for various TextView parameters and many buttons, there are
         * multiple ways calling code could configure this Fragment. The Builder pattern could be
         * used. Or new `Text` and `Button` data classes could be passed in to `newInstance()`. A
         * Kotlin DSL could even be created for setting all these parameters.
         * However, despite the large number of parameters, I went with passing individual
         * parameters to this function. Why? In a production environment, very few parameters would
         * be set in 99% of cases, and in that case calling something like
         * ```
         * AlertDialogFragment.newInstance(
         *   titleText = "Hop, Skip, or Drive?",
         *   button1Title = "Hop",
         *   button2Title = "Skip",
         *   button3Title = "Drive",
         * )
         * ```
         * would be more concise than the alternative methods.
         *
         */
        fun newInstance(
            titleText: String? = null,
            @ColorRes titleTextColor: Int? = null,
            titleAlignment: Int? = null,
            @FontRes titleFont: Int? = null,
            subtitleText: String? = null,
            @ColorRes subtitleTextColor: Int? = null,
            subtitleAlignment: Int? = null,
            @FontRes subtitleFont: Int? = null,
            button1Title: String? = null,
            button2Title: String? = null,
            button3Title: String? = null,
            button4Title: String? = null,
            button5Title: String? = null,
            button6Title: String? = null
        ): AlertDialogFragment {
            val fragment = AlertDialogFragment()
            val args = Bundle()

            titleText?.let { args.putString(KEY_TITLE_TEXT, it) }
            titleTextColor?.let { args.putInt(KEY_TITLE_COLOR, it) }
            titleAlignment?.let { args.putInt(KEY_TITLE_ALIGNMENT, it) }
            titleFont?.let { args.putInt(KEY_TITLE_FONT, it) }
            subtitleText?.let { args.putString(KEY_SUBTITLE_TEXT, it) }
            subtitleTextColor?.let { args.putInt(KEY_SUBTITLE_COLOR, it) }
            subtitleAlignment?.let { args.putInt(KEY_SUBTITLE_ALIGNMENT, it) }
            subtitleFont?.let { args.putInt(KEY_SUBTITLE_FONT, it) }
            button1Title?.let { args.putString(KEY_BUTTON_1_TEXT, it) }
            button2Title?.let { args.putString(KEY_BUTTON_2_TEXT, it) }
            button3Title?.let { args.putString(KEY_BUTTON_3_TEXT, it) }
            button4Title?.let { args.putString(KEY_BUTTON_4_TEXT, it) }
            button5Title?.let { args.putString(KEY_BUTTON_5_TEXT, it) }
            button6Title?.let { args.putString(KEY_BUTTON_6_TEXT, it) }

            fragment.arguments = args
            return fragment
        }
    }

    interface Listener {
        fun onButton1Click() {}
        fun onButton2Click() {}
        fun onButton3Click() {}
        fun onButton4Click() {}
        fun onButton5Click() {}
        fun onButton6Click() {}
    }
}