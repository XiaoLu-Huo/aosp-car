/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.car.storagemonitoring;

import static com.android.car.internal.ExcludeFromCodeCoverageGeneratedReport.BOILERPLATE_CODE;

import android.annotation.SystemApi;
import android.car.storagemonitoring.IoStatsEntry.Metrics;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonWriter;

import com.android.car.internal.ExcludeFromCodeCoverageGeneratedReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Delta of uid_io stats taken at a sample point.
 *
 * @hide
 *
 * @deprecated use {@link android.car.watchdog.CarWatchdogManager} and its related classes
 * for I/O related tasks.
 */
@Deprecated
@SystemApi
public final class IoStats implements Parcelable {
    public static final Creator<IoStats> CREATOR = new Creator<IoStats>() {
        @Override
        public IoStats createFromParcel(Parcel in) {
            return new IoStats(in);
        }

        @Override
        public IoStats[] newArray(int size) {
            return new IoStats[size];
        }
    };

    private final List<IoStatsEntry> mStats;
    private final long mUptimeTimestamp;

    public IoStats(List<IoStatsEntry> stats, long timestamp) {
        mStats = stats;
        mUptimeTimestamp = timestamp;
    }

    public IoStats(Parcel in) {
        mStats = in.createTypedArrayList(IoStatsEntry.CREATOR);
        mUptimeTimestamp = in.readLong();
    }

    /**
     * @hide
     */
    public IoStats(JSONObject in) throws JSONException {
        mUptimeTimestamp = in.getInt("uptime");
        JSONArray statsArray = in.getJSONArray("stats");
        mStats = new ArrayList<>();
        for (int i = 0; i < statsArray.length(); ++i) {
            mStats.add(new IoStatsEntry(statsArray.getJSONObject(i)));
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mStats);
        dest.writeLong(mUptimeTimestamp);
    }

    /**
     * @hide
     */
    public void writeToJson(JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("uptime").value(mUptimeTimestamp);
        jsonWriter.name("stats").beginArray();
        for (IoStatsEntry stat : mStats) {
            stat.writeToJson(jsonWriter);
        }
        jsonWriter.endArray();
        jsonWriter.endObject();
    }

    @Override
    @ExcludeFromCodeCoverageGeneratedReport(reason = BOILERPLATE_CODE)
    public int describeContents() {
        return 0;
    }

    public long getTimestamp() {
        return mUptimeTimestamp;
    }

    public List<IoStatsEntry> getStats() {
        return mStats;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mStats, mUptimeTimestamp);
    }

    /**
     * Returns user's stats ({@link IoStatsEntry}).
     *
     * @param uid Android's user id
     */
    public IoStatsEntry getUserIdStats(int uid) {
        for (IoStatsEntry stats : getStats()) {
            if (stats.uid == uid) {
                return stats;
            }
        }

        return null;
    }

    /**
     * Returns the following foreground total metrics: bytes written and read, bytes read from and
     * written to storage, and number of sync calls.
     */
    public Metrics getForegroundTotals() {
        long bytesRead = 0;
        long bytesWritten = 0;
        long bytesReadFromStorage = 0;
        long bytesWrittenToStorage = 0;
        long fsyncCalls = 0;

        for (IoStatsEntry stats : getStats()) {
            bytesRead += stats.foreground.bytesRead;
            bytesWritten += stats.foreground.bytesWritten;
            bytesReadFromStorage += stats.foreground.bytesReadFromStorage;
            bytesWrittenToStorage += stats.foreground.bytesWrittenToStorage;
            fsyncCalls += stats.foreground.fsyncCalls;
        }

        return new Metrics(bytesRead,
                bytesWritten,
                bytesReadFromStorage,
                bytesWrittenToStorage,
                fsyncCalls);
    }

    /**
     * Returns the following background total metrics: bytes written and read, bytes read from and
     * written to storage, and number of sync calls.
     */
    public Metrics getBackgroundTotals() {
        long bytesRead = 0;
        long bytesWritten = 0;
        long bytesReadFromStorage = 0;
        long bytesWrittenToStorage = 0;
        long fsyncCalls = 0;

        for (IoStatsEntry stats : getStats()) {
            bytesRead += stats.background.bytesRead;
            bytesWritten += stats.background.bytesWritten;
            bytesReadFromStorage += stats.background.bytesReadFromStorage;
            bytesWrittenToStorage += stats.background.bytesWrittenToStorage;
            fsyncCalls += stats.background.fsyncCalls;
        }

        return new Metrics(bytesRead,
            bytesWritten,
            bytesReadFromStorage,
            bytesWrittenToStorage,
            fsyncCalls);
    }

    /**
     * Returns the sum of all foreground and background metrics (bytes written, bytes read from
     * storage, bytes written to storage and number of sync calls).
     */
    public Metrics getTotals() {
        Metrics foreground = getForegroundTotals();
        Metrics background = getBackgroundTotals();

        return new Metrics(foreground.bytesRead + background.bytesRead,
                foreground.bytesWritten + background.bytesWritten,
                foreground.bytesReadFromStorage + background.bytesReadFromStorage,
                foreground.bytesWrittenToStorage + background.bytesWrittenToStorage,
                foreground.fsyncCalls + background.fsyncCalls);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof IoStats) {
            IoStats delta = (IoStats) other;
            return delta.getTimestamp() == getTimestamp()
                && delta.getStats().equals(getStats());
        }
        return false;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (IoStatsEntry stats : getStats()) {
            stringJoiner.add(stats.toString());
        }
        return "timestamp = " + getTimestamp() + ", stats = " + stringJoiner.toString();
    }
}
