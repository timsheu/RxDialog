package com.timsheu.simplerxdialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.support.annotation.ArrayRes
import android.support.annotation.AttrRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ListAdapter
import io.reactivex.*
import io.reactivex.annotations.NonNull
import java.util.ArrayList

class SimpleRxDialog {
    val POSITIVE = -1
    val NEGATIVE = -2
    val NEUTRAL = -3

    private lateinit var builder: AlertDialog.Builder
    private var positiveText: String? = null
    private var negativeText: String? = null
    private var neutralText: String? = null
    private lateinit var context: Context
    private var view: View? = null
    private val idList = ArrayList<Int>()

    fun SimpleRxDialog(mActivity: Context) {
        this.context = mActivity
        builder = AlertDialog.Builder(mActivity)
    }

    fun setPositiveText(@StringRes textId: Int): SimpleRxDialog {
        val text = context.getString(textId)
        setPositiveText(text)
        return this
    }

    fun setPositiveText(text: CharSequence): SimpleRxDialog {
        positiveText = text as String
        return this
    }

    fun setNegativeText(@StringRes textId: Int): SimpleRxDialog {
        val text = context.getString(textId)
        setNegativeText(text)
        return this
    }

    fun setNeutralText(@StringRes textId: Int): SimpleRxDialog {
        val text = context.getString(textId)
        setNeutralText(text)
        return this
    }

    fun setNeutralText(text: CharSequence): SimpleRxDialog {
        neutralText = text as String
        return this
    }

    fun setNegativeText(text: CharSequence): SimpleRxDialog {
        negativeText = text as String
        return this
    }

    fun clickView(viewId: Int): SimpleRxDialog {
        idList.add(viewId)
        return this
    }

    fun dialogToObservable(): Observable<*> {
        return Observable.create(ObservableOnSubscribe<Int> { e ->
            val onClickListener = DialogInterface.OnClickListener { dialogInterface, i ->
                when (i) {
                    -1 -> e.onNext(POSITIVE)
                    -2 -> e.onNext(NEGATIVE)
                    -3 -> e.onNext(NEUTRAL)
                    else -> e.onNext(i)
                }
            }
            val mviewListener = View.OnClickListener { v -> e.onNext(v.id) }
            if (view != null) {
                for (id in idList) {
                    view!!.findViewById<View>(id).setOnClickListener(mviewListener)
                }
            }
            builder.setPositiveButton(positiveText, onClickListener)
            builder.setNegativeButton(negativeText, onClickListener)
            builder.setNeutralButton(neutralText, onClickListener)
            builder.show()
        })
    }

    fun dialogToFlowable(): Flowable<*> {
        return Flowable.create(FlowableOnSubscribe<Any> { emitter ->
            val onClickListener = DialogInterface.OnClickListener { dialogInterface, i ->
                when (i) {
                    -1 -> emitter.onNext(POSITIVE)
                    -2 -> emitter.onNext(NEGATIVE)
                    -3 -> emitter.onNext(NEUTRAL)
                    else -> emitter.onNext(i)
                }
            }
            val mviewListener = View.OnClickListener { v -> emitter.onNext(v.id) }
            if (view != null) {
                for (id in idList) {
                    view!!.findViewById<View>(id).setOnClickListener(mviewListener)
                }
            }
            builder.setPositiveButton(positiveText, onClickListener)
            builder.setNegativeButton(negativeText, onClickListener)
            builder.setNeutralButton(neutralText, onClickListener)
            builder.show()
        }, BackpressureStrategy.BUFFER)
    }

    /**
     * Set the title using the given resource id.
     */
    fun setTitle(@StringRes titleId: Int): SimpleRxDialog {
        builder.setTitle(titleId)
        return this
    }

    /**
     * Set the title displayed in the [Dialog].
     */
    fun setTitle(title: CharSequence?): SimpleRxDialog {
        builder.setTitle(title)
        return this
    }

    /**
     * Set the title using the custom view `customTitleView`.
     */
    fun setCustomTitle(customTitleView: View?): SimpleRxDialog {
        builder.setCustomTitle(customTitleView)
        return this
    }

    /**
     * Set the message to display using the given resource id.
     */
    fun setMessage(@StringRes messageId: Int): SimpleRxDialog {
        builder.setMessage(messageId)
        return this
    }

    /**
     * Set the message to display.
     */
    fun setMessage(message: CharSequence?): SimpleRxDialog {
        builder.setMessage(message)
        return this
    }

    /**
     * Set the resource id of the [Drawable] to be used in the title.
     */
    fun setIcon(@DrawableRes iconId: Int): SimpleRxDialog {
        builder.setIcon(iconId)
        return this
    }

    /**
     * Set the [Drawable] to be used in the title.
     */
    fun setIcon(icon: Drawable?): SimpleRxDialog {
        builder.setIcon(icon)
        return this
    }

    /**
     * Set an icon as supplied by a theme attribute. e.g.
     */
    fun setIconAttribute(@AttrRes attrId: Int): SimpleRxDialog {
        builder.setIconAttribute(attrId)
        return this
    }

    /**
     * Sets whether the dialog is cancelable or not.  Default is true.
     */
    fun setCancelable(cancelable: Boolean): SimpleRxDialog {
        builder.setCancelable(cancelable)
        return this
    }

    /**
     * Sets the callback that will be called if the dialog is canceled.
     */
    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): SimpleRxDialog {
        builder.setOnCancelListener(onCancelListener)
        return this
    }

    /**
     * Sets the callback that will be called when the dialog is dismissed for any reason.
     */
    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): SimpleRxDialog {
        builder.setOnDismissListener(onDismissListener)
        return this
    }

    /**
     * Sets the callback that will be called if a key is dispatched to the dialog.
     */
    fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener): SimpleRxDialog {
        builder.setOnKeyListener(onKeyListener)
        return this
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you will be notified of the
     * selected item via the supplied listener. This should be an array type i.e. R.array.foo
     */
    fun setItems(@ArrayRes itemsId: Int, listener: DialogInterface.OnClickListener): SimpleRxDialog {
        builder.setItems(itemsId, listener)
        return this
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you will be notified of the
     * selected item via the supplied listener.
     */
    fun setItems(items: Array<CharSequence>, listener: DialogInterface.OnClickListener): SimpleRxDialog {
        builder.setItems(items, listener)
        return this
    }

    /**
     * Set a list of items, which are supplied by the given [ListAdapter], to be
     * displayed in the dialog as the content, you will be notified of the
     * selected item via the supplied listener.
     */
    fun setAdapter(adapter: ListAdapter, listener: DialogInterface.OnClickListener): SimpleRxDialog {
        builder.setAdapter(adapter, listener)
        return this
    }

    /**
     * Set a list of items, which are supplied by the given [Cursor], to be
     * displayed in the dialog as the content, you will be notified of the
     * selected item via the supplied listener.
     */
    fun setCursor(cursor: Cursor, listener: DialogInterface.OnClickListener,
                  labelColumn: String): SimpleRxDialog {
        builder.setCursor(cursor, listener, labelColumn)
        return this
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * This should be an array type, e.g. R.array.foo. The list will have
     * a check mark displayed to the right of the text for each checked
     * item. Clicking on an item in the list will not dismiss the dialog.
     * Clicking on a button will dismiss the dialog.
     */
    fun setMultiChoiceItems(@ArrayRes itemsId: Int, checkedItems: BooleanArray,
                            listener: DialogInterface.OnMultiChoiceClickListener): SimpleRxDialog {
        builder.setMultiChoiceItems(itemsId, checkedItems, listener)
        return this
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * The list will have a check mark displayed to the right of the text
     * for each checked item. Clicking on an item in the list will not
     * dismiss the dialog. Clicking on a button will dismiss the dialog.
     */
    fun setMultiChoiceItems(items: Array<CharSequence>, checkedItems: BooleanArray,
                            listener: DialogInterface.OnMultiChoiceClickListener): SimpleRxDialog {
        builder.setMultiChoiceItems(items, checkedItems, listener)
        return this
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * The list will have a check mark displayed to the right of the text
     * for each checked item. Clicking on an item in the list will not
     * dismiss the dialog. Clicking on a button will dismiss the dialog.
     */
    fun setMultiChoiceItems(cursor: Cursor, isCheckedColumn: String, labelColumn: String,
                            listener: DialogInterface.OnMultiChoiceClickListener): SimpleRxDialog {
        builder.setMultiChoiceItems(cursor, isCheckedColumn, labelColumn, listener)
        return this
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you will be notified of
     * the selected item via the supplied listener. This should be an array type i.e.
     * R.array.foo The list will have a check mark displayed to the right of the text for the
     * checked item. Clicking on an item in the list will not dismiss the dialog. Clicking on a
     * button will dismiss the dialog.
     */
    fun setSingleChoiceItems(@ArrayRes itemsId: Int, checkedItem: Int,
                             listener: DialogInterface.OnClickListener): SimpleRxDialog {
        builder.setSingleChoiceItems(itemsId, checkedItem, listener)
        return this
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you will be notified of
     * the selected item via the supplied listener. The list will have a check mark displayed to
     * the right of the text for the checked item. Clicking on an item in the list will not
     * dismiss the dialog. Clicking on a button will dismiss the dialog.
     */
    fun setSingleChoiceItems(cursor: Cursor, checkedItem: Int, labelColumn: String,
                             listener: DialogInterface.OnClickListener): SimpleRxDialog {
        builder.setSingleChoiceItems(cursor, checkedItem, labelColumn, listener)
        return this
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you will be notified of
     * the selected item via the supplied listener. The list will have a check mark displayed to
     * the right of the text for the checked item. Clicking on an item in the list will not
     * dismiss the dialog. Clicking on a button will dismiss the dialog.
     */
    fun setSingleChoiceItems(items: Array<CharSequence>, checkedItem: Int, listener: DialogInterface.OnClickListener): SimpleRxDialog {
        builder.setSingleChoiceItems(items, checkedItem, listener)
        return this
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you will be notified of
     * the selected item via the supplied listener. The list will have a check mark displayed to
     * the right of the text for the checked item. Clicking on an item in the list will not
     * dismiss the dialog. Clicking on a button will dismiss the dialog.
     */
    fun setSingleChoiceItems(adapter: ListAdapter, checkedItem: Int, listener: DialogInterface.OnClickListener): SimpleRxDialog {
        builder.setSingleChoiceItems(adapter, checkedItem, listener)
        return this
    }

    /**
     * Sets a listener to be invoked when an item in the list is selected.
     * @see AdapterView.setOnItemSelectedListener
     */
    fun setOnItemSelectedListener(listener: AdapterView.OnItemSelectedListener): SimpleRxDialog {
        builder.setOnItemSelectedListener(listener)
        return this
    }

    /**
     * Set a custom view resource to be the contents of the Dialog. The
     * resource will be inflated, adding all top-level views to the screen.
     */
    fun setView(layoutResId: Int): SimpleRxDialog {
        //builder.setView(layoutResId);
        val view = LayoutInflater.from(context).inflate(layoutResId, null, false)
        return setView(view)
    }

    /**
     * Sets a custom view to be the contents of the alert dialog.
     */
    fun setView(view: View): SimpleRxDialog {
        builder.setView(view)
        this.view = view
        return this
    }

    /**
     * Sets the Dialog to use the inverse background, regardless of what the
     * contents is.
     */
    @Deprecated("")
    fun setInverseBackgroundForced(useInverseBackground: Boolean): SimpleRxDialog {
        builder.setInverseBackgroundForced(useInverseBackground)
        return this
    }
}