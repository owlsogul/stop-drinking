package owlsogul.blog.me.stopdrinking.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import owlsogul.blog.me.stopdrinking.R;

public class AdapterFirstFeedback extends BaseAdapter {

    private ArrayList<ItemFirstFeedback> items;

    public AdapterFirstFeedback(){
        items = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_first_feedback, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView labelSleepHour = convertView.findViewById(R.id.labelSleepHour);
        TextView labelTension = convertView.findViewById(R.id.labelTension);
        TextView labelDrinkingYesterday = convertView.findViewById(R.id.labelDrinkingYesterday);
        TextView labelAmountDrink = convertView.findViewById(R.id.labelAmountDrink);
        TextView labelDrinkness = convertView.findViewById(R.id.labelDrinkness);

        ItemFirstFeedback item = items.get(pos);
        labelSleepHour.setText(String.format("수면량: %d시간, ", item.getSleepHour()));
        labelTension.setText(String.format("긴장도 : %d, ", item.getTension()));
        labelDrinkingYesterday.setText(String.format("전날 마신 술의 양: %d잔, ", item.getDrinkingYesterday()));
        labelAmountDrink.setText(String.format("음주량: %d잔, ", item.getAmountDrink()));
        labelDrinkness.setText(String.format("취한 정도: %d", item.getDrinkness()));
        return convertView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addItem(int sleepHour, int tension, int drinkingYesterday, int amountDrink, int drinkness){
        ItemFirstFeedback item = new ItemFirstFeedback();
        item.setSleepHour(sleepHour);
        item.setTension(tension);
        item.setDrinkingYesterday(drinkingYesterday);
        item.setAmountDrink(amountDrink);
        item.setDrinkness(drinkness);
        items.add(item);
    }

    public void removeItem(int idx){
        items.remove(idx);
    }

    public ArrayList<ItemFirstFeedback> getItems(){
        return items;
    }

}
