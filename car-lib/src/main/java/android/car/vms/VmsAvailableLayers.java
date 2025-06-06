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

package android.car.vms;

import static com.android.car.internal.ExcludeFromCodeCoverageGeneratedReport.BOILERPLATE_CODE;

import android.annotation.NonNull;
import android.annotation.SystemApi;
import android.car.builtin.os.ParcelHelper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArraySet;

import com.android.car.internal.ExcludeFromCodeCoverageGeneratedReport;
import com.android.car.internal.util.AnnotationValidations;

import java.util.Collections;
import java.util.Set;

/**
 * Availability of Vehicle Map Service layers.
 *
 * The layer availability is used by subscribers to determine which {@link VmsLayer}s are available
 * for subscription and which publishers are offering to publish data for those layers. However,
 * the Vehicle Map Service will allow subscription requests for unavailable layers.
 *
 * Sequence numbers are used to indicate the succession of availability states, and increase
 * monotonically with each change in layer availability. They must be used by clients to ignore
 * states that are received out-of-order.
 *
 * @hide
 */
@SystemApi
public final class VmsAvailableLayers implements Parcelable {
    /**
     * Sequence number of the availability state
     */
    private final int mSequenceNumber;

    /**
     * Set of layers available for subscription
     */
    private @NonNull Set<VmsAssociatedLayer> mAssociatedLayers;

    private void onConstructed() {
        mAssociatedLayers = Collections.unmodifiableSet(mAssociatedLayers);
    }

    private void parcelAssociatedLayers(Parcel dest) {
        ParcelHelper.writeArraySet(dest, new ArraySet<>(mAssociatedLayers));
    }

    @SuppressWarnings("unchecked")
    private Set<VmsAssociatedLayer> unparcelAssociatedLayers(Parcel in) {
        return (Set<VmsAssociatedLayer>) ParcelHelper.readArraySet(in,
                VmsAssociatedLayer.class.getClassLoader());
    }

    /**
     * Creates a new VmsAvailableLayers.
     *
     * @param associatedLayers
     *   Set of layers available for subscription
     * @param sequenceNumber
     *   Sequence number of the availability state
     * @deprecated Use {@link #VmsAvailableLayers(int, Set)} instead
     */
    @Deprecated
    public VmsAvailableLayers(@NonNull Set<VmsAssociatedLayer> associatedLayers,
            int sequenceNumber) {
        this(sequenceNumber, associatedLayers);
    }

    /**
     * Sequence number of the availability state
     *
     * @deprecated Use {@link #getSequenceNumber()} instead
     */
    @Deprecated
    public int getSequence() {
        return mSequenceNumber;
    }

    /**
     * Creates a new VmsAvailableLayers.
     *
     * @param sequenceNumber
     *   Sequence number of the availability state
     * @param associatedLayers
     *   Set of layers available for subscription
     */
    public VmsAvailableLayers(
            int sequenceNumber,
            @NonNull Set<VmsAssociatedLayer> associatedLayers) {
        this.mSequenceNumber = sequenceNumber;
        this.mAssociatedLayers = associatedLayers;
        AnnotationValidations.validate(
                NonNull.class, null, mAssociatedLayers);

        onConstructed();
    }

    /**
     * Sequence number of the availability state
     */
    public int getSequenceNumber() {
        return mSequenceNumber;
    }

    /**
     * Set of layers available for subscription
     */
    public @NonNull Set<VmsAssociatedLayer> getAssociatedLayers() {
        return mAssociatedLayers;
    }

    @Override
    public String toString() {
        // You can override field toString logic by defining methods like:
        // String fieldNameToString() { ... }

        return "VmsAvailableLayers { " +
                "sequenceNumber = " + mSequenceNumber + ", " +
                "associatedLayers = " + mAssociatedLayers +
        " }";
    }

    @Override
    public boolean equals(@android.annotation.Nullable Object o) {
        // You can override field equality logic by defining either of the methods like:
        // boolean fieldNameEquals(VmsAvailableLayers other) { ... }
        // boolean fieldNameEquals(FieldType otherValue) { ... }

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        @SuppressWarnings("unchecked")
        VmsAvailableLayers that = (VmsAvailableLayers) o;
        //noinspection PointlessBooleanExpression
        return true
                && mSequenceNumber == that.mSequenceNumber
                && java.util.Objects.equals(mAssociatedLayers, that.mAssociatedLayers);
    }

    @Override
    public int hashCode() {
        // You can override field hashCode logic by defining methods like:
        // int fieldNameHashCode() { ... }

        int _hash = 1;
        _hash = 31 * _hash + mSequenceNumber;
        _hash = 31 * _hash + java.util.Objects.hashCode(mAssociatedLayers);
        return _hash;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        // You can override field parcelling by defining methods like:
        // void parcelFieldName(Parcel dest, int flags) { ... }

        dest.writeInt(mSequenceNumber);
        parcelAssociatedLayers(dest);
    }

    @Override
    @ExcludeFromCodeCoverageGeneratedReport(reason = BOILERPLATE_CODE)
    public int describeContents() { return 0; }

    /** @hide */
    @SuppressWarnings({"unchecked", "RedundantCast"})
    /* package-private */ VmsAvailableLayers(@NonNull Parcel in) {
        // You can override field unparcelling by defining methods like:
        // static FieldType unparcelFieldName(Parcel in) { ... }

        int sequenceNumber = in.readInt();
        Set<VmsAssociatedLayer> associatedLayers = unparcelAssociatedLayers(in);

        this.mSequenceNumber = sequenceNumber;
        this.mAssociatedLayers = associatedLayers;
        AnnotationValidations.validate(
                NonNull.class, null, mAssociatedLayers);

        onConstructed();
    }

    public static final @NonNull Creator<VmsAvailableLayers> CREATOR
            = new Creator<VmsAvailableLayers>() {
        @Override
        public VmsAvailableLayers[] newArray(int size) {
            return new VmsAvailableLayers[size];
        }

        @Override
        public VmsAvailableLayers createFromParcel(@NonNull Parcel in) {
            return new VmsAvailableLayers(in);
        }
    };
}
