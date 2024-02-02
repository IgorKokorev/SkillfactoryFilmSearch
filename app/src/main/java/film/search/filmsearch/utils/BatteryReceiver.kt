package film.search.filmsearch.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import film.search.filmsearch.R

class BatteryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        if (intent.action.equals(Intent.ACTION_BATTERY_LOW)) {
            Toast.makeText(context, context?.getString(R.string.battery_low_toast), Toast.LENGTH_LONG).show()
        } else if (intent.action.equals(Intent.ACTION_POWER_CONNECTED)) {
            Toast.makeText(context,
                context?.getString(R.string.power_cable_connected_toast), Toast.LENGTH_LONG).show()
        }
    }
}
