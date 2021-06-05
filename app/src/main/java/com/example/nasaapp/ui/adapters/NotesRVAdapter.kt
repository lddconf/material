package com.example.nasaapp.ui.adapters

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaapp.R
import com.example.nasaapp.databinding.NotePreviewLayoutBinding
import com.example.nasaapp.model.data.NasaAppNote
import com.example.nasaapp.ui.utils.toColorResId
import java.text.SimpleDateFormat
import java.util.*

interface OnItemActionListener {
    fun onItemClick(note: NasaAppNote)
    fun onItemDelete(note: NasaAppNote)
}

class NotesRVAdapter(
    var onItemActionListener: OnItemActionListener? = null
) : RecyclerView.Adapter<NotesRVAdapter.NoteRVHolder>() {

    private val notes: MutableList<NasaAppNote> = mutableListOf()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteRVHolder {
        return NoteRVHolder(
            NotePreviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteRVHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    fun setNotes(newNotes: List<NasaAppNote>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(notes, newNotes))
        result.dispatchUpdatesTo(this)
        notes.clear()
        notes.addAll(newNotes)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        notes.removeAt(fromPosition).apply {
            notes.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }


    inner class NoteRVHolder(val ui: NotePreviewLayoutBinding) : RecyclerView.ViewHolder(ui.root) {
        fun bind(note: NasaAppNote) = with(ui) {
            noteTitle.text = note.title
            noteDate.text = SimpleDateFormat(
                ui.root.context.getString(R.string.date_format),
                Locale.getDefault()
            ).format(note.lastChanged)
            noteBody.text = note.text

            root.setCardBackgroundColor(
                ResourcesCompat.getColor(
                    root.resources,
                    note.color.toColorResId(),
                    null
                )
            );
            root.setOnClickListener {
                onItemActionListener?.onItemClick(note)
            }
        }
    }

    inner class NoteRVSwipeToDelete(
        private val adapter: NotesRVAdapter,
        private val icon: Drawable
    ) : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT
    ) {
        private val background = ColorDrawable(Color.RED);

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(
                dragFlags,
                swipeFlags
            )
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            val itemView = viewHolder.itemView
            val backgroundCornerOffset = 20
            val iconMargin = (itemView.getHeight() - icon.intrinsicHeight) / 2;
            val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2;
            val iconBottom = iconTop + icon.intrinsicHeight;

            if (dX < 0) { // Swiping to the left
                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom
                )
            } else { // view is unSwiped
                background.setBounds(0, 0, 0, 0)
            }

            background.draw(c)
            icon.draw(c)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.getAdapterPosition();
            adapter.onItemActionListener?.apply {
                notifyItemRemoved(position)
                onItemDelete(notes[position])
            }
        }
    }

    inner class DiffUtilCallback(
        private var oldItems: List<NasaAppNote>,
        private var newItems: List<NasaAppNote>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].uid == newItems[newItemPosition].uid

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            (oldItems[oldItemPosition].color == newItems[newItemPosition].color) &&
                    (oldItems[oldItemPosition].title == newItems[newItemPosition].title) &&
                    (oldItems[oldItemPosition].text == newItems[newItemPosition].text) &&
                    (oldItems[oldItemPosition].lastChanged == newItems[newItemPosition].lastChanged)

    }
}