package costy.krzysiekz.com.costy.view.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krzysiekz.costy.model.ReportEntry;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import costy.krzysiekz.com.costy.R;

public class ReportEntryAdapter extends RecyclerView.Adapter<ReportEntryAdapter.ViewHolder> {

    private String amountCurrencyPattern;

    private List<ReportEntry> reportEntries = new ArrayList<>();

    @Override
    public ReportEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report_entry, parent, false);
        amountCurrencyPattern = parent.getResources().getString(R.string.amount_currency_pattern);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportEntryAdapter.ViewHolder holder, int position) {
        ReportEntry entry = reportEntries.get(position);
        holder.reportEntryFrom.setText(entry.getSender().getName());
        holder.reportEntryTo.setText(entry.getReceiver().getName());
        holder.reportEntryAmount.setText(MessageFormat.format(amountCurrencyPattern,
                entry.getAmount().toPlainString(), entry.getCurrency().getName()));
    }

    @Override
    public int getItemCount() {
        return reportEntries.size();
    }

    public List<ReportEntry> getReportEntries() {
        return reportEntries;
    }

    public void setReportEntries(List<ReportEntry> reportEntries) {
        this.reportEntries = reportEntries;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reportEntryFrom;
        TextView reportEntryTo;
        TextView reportEntryAmount;

        ViewHolder(View view) {
            super(view);
            reportEntryFrom = (TextView) view.findViewById(R.id.item_report_entry_from);
            reportEntryTo = (TextView) view.findViewById(R.id.item_report_entry_to);
            reportEntryAmount = (TextView) view.findViewById(R.id.item_report_entry_amount);
        }
    }
}
