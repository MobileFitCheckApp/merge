package isy.mjc.fitcheckapp_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ManageListAdapter extends BaseAdapter {

    //TODO: 커스텀 리스트뷰 만듦, 삭제버튼->해당리스트누르면 삭제
    ArrayList<ManageListItem> items = new ArrayList<>();
    Context context;

    public ManageListAdapter() {
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        ManageListItem listItem = items.get(position);

        if (convertView == null) {
            //context에서 layoutinflater를 가져오는 방법
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_manage_member, parent, false);
        }
        //화면에 보여질 데이터를 참조합니다.
        TextView memName = convertView.findViewById(R.id.memName);
        TextView memID = convertView.findViewById(R.id.memID);
        ImageButton deleteBtn = convertView.findViewById(R.id.deleteBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(listItem);
                notifyDataSetChanged(); // 어댑터에 변경을 알림
            }
        });
        //데이터를 set해줍니다.
        memName.setText(listItem.getMemName());
        memID.setText(listItem.getMemID());
        return convertView;
    }
    public void addItem(ManageListItem item) {
        items.add(item);
    }
    public void remove(ManageListItem item){
        items.remove(item);
    }
}
