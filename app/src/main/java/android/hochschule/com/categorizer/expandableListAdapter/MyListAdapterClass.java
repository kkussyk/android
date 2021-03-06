package android.hochschule.com.categorizer.expandableListAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hochschule.com.categorizer.R;
import android.hochschule.com.categorizer.category.CategoryClass;
import android.hochschule.com.categorizer.category.CategoryEditActivity;
import android.hochschule.com.categorizer.item.ItemClass;
import android.hochschule.com.categorizer.item.ItemEditActivity;
import android.hochschule.com.categorizer.main.MainActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Klasse dient als Adapter für die Expandable List View.
 * Neben der Darstellung der Group und Child Elementen sind diverse Funktionen implementiert.
 */
public class MyListAdapterClass extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<CategoryClass> categories;

    public MyListAdapterClass(Context context, ArrayList<CategoryClass> categories) {
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final CategoryClass category = (CategoryClass) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_group_item, null);
        }

        TextView header = (TextView) convertView.findViewById(R.id.grpItem);
        header.setText(category.getName().trim());
        ImageView openMenu = (ImageView) convertView.findViewById(R.id.grpDots);    //3 Punkte Bild als klickbares Menü

        openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            //Kategorie editieren
                            case R.id.mnuEdit:
                                ((MainActivity) context).setCurrentGrpPos(groupPosition);
                                Intent intent = new Intent(context, CategoryEditActivity.class);
                                intent.putExtra("category", category);
                                ((MainActivity) context).startActivityForResult(intent, 1);
                                return true;
                            //Kategorie löschen
                            case R.id.mnuDelete:
                                final AlertDialog alert = new AlertDialog.Builder(context).create();
                                alert.setTitle(context.getResources().getString(R.string.dialog_title_delete));
                                alert.setMessage(context.getResources().getString(R.string.dialog_msg_category) + " \"" + category.getName() + "\" " + context.getResources().getString(R.string.dialog_msg_realy_delete));
                                alert.setIcon(R.drawable.ic_dialog_warning);
                                alert.setCancelable(false);
                                alert.setButton(Dialog.BUTTON_POSITIVE, context.getResources().getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String categoryName = category.getName();
                                        categories.remove(category);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "\"" + categoryName + "\" " + context.getResources().getString(R.string.toast_deleted), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                alert.setButton(Dialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alert.show();
                                return true;
                            //Item dieser Kategorie hinzufügen
                            case R.id.mnuAdd:
                                ((MainActivity) context).setCurrentGrpPos(groupPosition);
                                ((MainActivity) context).setCurrentChildPos(-1);
                                Intent intentItem = new Intent(context, ItemEditActivity.class);
                                intentItem.putExtra("item", new ItemClass(""));
                                ((MainActivity) context).startActivityForResult(intentItem, 0);
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

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
        ImageView delete = (ImageView) convertView.findViewById(R.id.delItem);  //Mültonnen Bild als klickbare Item-Löschfunktion

        final int grpID = groupPosition;
        final int childID = childPosition;

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setTitle(context.getResources().getString(R.string.dialog_title_delete));
                alert.setMessage("\"" + item.getName() + "\" " + context.getResources().getString(R.string.dialog_msg_realy_delete));
                alert.setIcon(R.drawable.ic_dialog_warning);
                alert.setCancelable(false);
                alert.setButton(Dialog.BUTTON_POSITIVE, context.getResources().getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<ItemClass> child = categories.get(grpID).getItems();
                        child.remove(childID);
                        notifyDataSetChanged();
                        Toast.makeText(context, "\"" + item.getName() + "\" " + context.getResources().getString(R.string.toast_deleted), Toast.LENGTH_SHORT).show();
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

        childHeader.setText(item.getName().trim());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}