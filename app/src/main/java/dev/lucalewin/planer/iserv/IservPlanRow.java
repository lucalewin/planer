package dev.lucalewin.planer.iserv;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class IservPlanRow {

    private final String hour;
    private final String missing;
    private final String replacing;
    private final String subject;
    private final String otherSubject;
    private final String room;
    private final String type;
    private final String movedFrom;
    private final String text;

    public IservPlanRow(String hour, String missing, String replacing, String subject, String otherSubject, String room, String type, String movedFrom, String text) {
        this.hour = hour;
        this.missing = missing;
        this.replacing = replacing;
        this.subject = subject;
        this.otherSubject = otherSubject;
        this.room = room;
        this.type = type;
        this.movedFrom = movedFrom;
        this.text = text;
    }

    public TableRow toTableRow(Context context) {
        TableRow tableRow = new TableRow(context);

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, 0, 40, 0);

        TextView hourTw = new TextView(context);
        hourTw.setLayoutParams(layoutParams);
        hourTw.setText(hour);
        tableRow.addView(hourTw);

        TextView missingTw = new TextView(context);
        missingTw.setLayoutParams(layoutParams);
        missingTw.setText(missing);
        tableRow.addView(missingTw);

        TextView courseTw = new TextView(context);
        courseTw.setLayoutParams(layoutParams);
        courseTw.setText(subject);
        tableRow.addView(courseTw);

        TextView typeTw = new TextView(context);
        typeTw.setLayoutParams(layoutParams);
        typeTw.setText(type);
        tableRow.addView(typeTw);

        TextView roomTw = new TextView(context);
        roomTw.setLayoutParams(layoutParams);
        roomTw.setText(room);
        tableRow.addView(roomTw);

        return tableRow;
    }

    @Override
    public String toString() {
        return "IservPlanRow{" +
                "hour='" + hour + '\'' +
                ", missing='" + missing + '\'' +
                ", replacing='" + replacing + '\'' +
                ", subject='" + subject + '\'' +
                ", otherSubject='" + otherSubject + '\'' +
                ", room='" + room + '\'' +
                ", type='" + type + '\'' +
                ", movedFrom='" + movedFrom + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public String getHour() {
        return hour;
    }

    public String getMissingTeacher() {
        return missing;
    }

    public String getReplacingTeacher() {
        return replacing;
    }

    public String getSubject() {
        return subject;
    }

    public String getOtherSubject() {
        return otherSubject;
    }

    public String getRoom() {
        return room;
    }

    public String getType() {
        return type;
    }

    public String getMovedFrom() {
        return movedFrom;
    }

    public String getText() {
        return text;
    }
}
