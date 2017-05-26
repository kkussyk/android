package android.hochschule.com.categorizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


class MyAdapterClass extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<CategoryClass> categories;

    MyAdapterClass(Context context, ArrayList<CategoryClass> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categories.get(groupPosition).getItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        CategoryClass category = (CategoryClass) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_group_item, null);
        }

        TextView header = (TextView) convertView.findViewById(R.id.grpItem);
        header.setText(category.getName().trim());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ItemClass item = (ItemClass) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_child_item, null);
        }

        TextView childHeader = (TextView) convertView.findViewById(R.id.childItem);
        ImageView delete = (ImageView) convertView.findViewById(R.id.delItem);
        ImageView edit = (ImageView) convertView.findViewById(R.id.editItem);

        final int grpID = groupPosition;
        final int childID = childPosition;

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setTitle(context.getResources().getString(R.string.dialog_title_caution));
                alert.setMessage("\"" + item.getName() + "\" wirklich löschen?");
                alert.setCancelable(false);
                alert.setButton(Dialog.BUTTON_POSITIVE, context.getResources().getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<ItemClass> child = categories.get(grpID).getItems();
                        child.remove(childID);
                        notifyDataSetChanged();
                        Toast.makeText(context, "\"" + item.getName() + "\" gelöscht", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setButton(Dialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "\"" + item.getName() + "\" bearbeiten", Toast.LENGTH_SHORT).show();
            }
        });

        childHeader.setText(item.getName().trim());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}