package com.example.pdvs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdvs.Adapter.DocsAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private DocsAdapter adapter;
    public RecyclerItemTouchHelper(DocsAdapter adapter){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction){
        final int position = viewHolder.getAbsoluteAdapterPosition();
        if (direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Document");
            builder.setMessage("Are you sure you want to delete this document?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteItem(position);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            adapter.editItem(position);
        }
    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable backGround;
        View itemView = viewHolder.itemView;
        int backGroundCornerOffset = 20;
        if (dX>0){
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.sharp_contract_edit_24);
            backGround = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.colorPrimary));
        }else{
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_delete_24);
            backGround = new ColorDrawable(Color.RED);
        }
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight())/2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight())/2;
        int iconBotom = iconTop + icon.getIntrinsicHeight();

        if (dX>0){
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRigth = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRigth, iconBotom);

            backGround.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backGroundCornerOffset, itemView.getBottom());
        }else if (dX<0){
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRigth = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRigth, iconBotom);

            backGround.setBounds(itemView.getRight() + ((int) dX) - backGroundCornerOffset,
                    itemView.getTop(),
                    itemView.getRight(),
                    itemView.getBottom());
        }else{
            backGround.setBounds(0, 0, 0, 0);
        }
        backGround.draw(c);
        icon.draw(c);
    }
}
