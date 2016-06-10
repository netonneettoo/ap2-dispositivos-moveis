package br.com.facevol.ap2dadm;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    protected SQLiteDatabase db;
    protected List<Task> list = new ArrayList<>();

    public TaskAdapter(SQLiteDatabase db, List<Task> list) {
        this.list = list;
        this.db = db;
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_recycler_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvDescription.setText(list.get(position).getDescription());
        final int positionFinal = holder.getAdapterPosition();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeItem(positionFinal);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Task task) {
        int position = list.size();
        list.add(task);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        try {
            Task task = list.get(position);
            db.execSQL(String.format("DELETE FROM tasks WHERE id = %s;", task.getId()));
            list.remove(task);
            notifyItemRemoved(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDescription;
        public ViewHolder(View v) {
            super(v);
            tvDescription = (TextView) v.findViewById(R.id.tvDescription);
        }
    }
}
