/*
 * Copyright (C) 2023 The Android Open Source Project
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

package android.car.remoteaccess;


import android.annotation.NonNull;
import android.os.Parcelable;

import com.android.car.internal.util.AnnotationValidations;
import com.android.car.internal.util.DataClass;

/**
 * Car remote task client registration info definition.
 */
@DataClass(genHiddenConstructor = true)
public final class RemoteTaskClientRegistrationInfo implements Parcelable {
    /**
     * Globally unique identifier to specify the wake-up server.
     */
    private final @NonNull String mServiceId;

    /**
     * Globally unique identifier to specify the vehicle.
     */
    private final @NonNull String mVehicleId;

    /**
     * Locally unique identifier to specify the processor where the task execution will be done.
     *
     * <p>There might be multiple processors running Android or other OS in single vehicle device.
     */
    private final @NonNull String mProcessorId;

    /**
     * Locally unique identifier to specify the remote task client.
     */
    private final @NonNull String mClientId;



    // Code below generated by codegen v1.0.23.
    //
    // DO NOT MODIFY!
    // CHECKSTYLE:OFF Generated code
    //
    // To regenerate run:
    // $ codegen $ANDROID_BUILD_TOP/packages/services/Car/car-lib/src/android/car/remoteaccess/RemoteTaskClientRegistrationInfo.java
    //
    // To exclude the generated code from IntelliJ auto-formatting enable (one-time):
    //   Settings > Editor > Code Style > Formatter Control
    //@formatter:off


    /**
     * Creates a new RemoteTaskClientRegistrationInfo.
     *
     * @param serviceId
     *   Globally unique identifier to specify the wake-up service.
     * @param vehicleId
     *   Globally unique identifier to specify the vehicle.
     * @param processorId
     *   Locally unique identifier to specify the processor where the task execution will be done.
     * @param clientId
     *   Locally unique identifier to specify the remote task client.
     * @hide
     */
    @DataClass.Generated.Member
    public RemoteTaskClientRegistrationInfo(
            @NonNull String serviceId,
            @NonNull String vehicleId,
            @NonNull String processorId,
            @NonNull String clientId) {
        this.mServiceId = serviceId;
        AnnotationValidations.validate(
                NonNull.class, null, mServiceId);
        this.mVehicleId = vehicleId;
        AnnotationValidations.validate(
                NonNull.class, null, mVehicleId);
        this.mProcessorId = processorId;
        AnnotationValidations.validate(
                NonNull.class, null, mProcessorId);
        this.mClientId = clientId;
        AnnotationValidations.validate(
                NonNull.class, null, mClientId);

        // onConstructed(); // You can define this method to get a callback
    }

    /**
     * Globally unique identifier to specify the wake-up service.
     */
    @DataClass.Generated.Member
    public @NonNull String getServiceId() {
        return mServiceId;
    }

    /**
     * Globally unique identifier to specify the vehicle.
     */
    @DataClass.Generated.Member
    public @NonNull String getVehicleId() {
        return mVehicleId;
    }

    /**
     * Locally unique identifier to specify the processor where the task execution will be done.
     */
    @DataClass.Generated.Member
    public @NonNull String getProcessorId() {
        return mProcessorId;
    }

    /**
     * Locally unique identifier to specify the remote task client.
     */
    @DataClass.Generated.Member
    public @NonNull String getClientId() {
        return mClientId;
    }

    @Override
    @DataClass.Generated.Member
    public void writeToParcel(@NonNull android.os.Parcel dest, int flags) {
        // You can override field parcelling by defining methods like:
        // void parcelFieldName(Parcel dest, int flags) { ... }

        dest.writeString(mServiceId);
        dest.writeString(mVehicleId);
        dest.writeString(mProcessorId);
        dest.writeString(mClientId);
    }

    @Override
    @DataClass.Generated.Member
    public int describeContents() { return 0; }

    /** @hide */
    @SuppressWarnings({"unchecked", "RedundantCast"})
    @DataClass.Generated.Member
    /* package-private */ RemoteTaskClientRegistrationInfo(@NonNull android.os.Parcel in) {
        // You can override field unparcelling by defining methods like:
        // static FieldType unparcelFieldName(Parcel in) { ... }

        String serviceId = in.readString();
        String vehicleId = in.readString();
        String processorId = in.readString();
        String clientId = in.readString();

        this.mServiceId = serviceId;
        AnnotationValidations.validate(
                NonNull.class, null, mServiceId);
        this.mVehicleId = vehicleId;
        AnnotationValidations.validate(
                NonNull.class, null, mVehicleId);
        this.mProcessorId = processorId;
        AnnotationValidations.validate(
                NonNull.class, null, mProcessorId);
        this.mClientId = clientId;
        AnnotationValidations.validate(
                NonNull.class, null, mClientId);

        // onConstructed(); // You can define this method to get a callback
    }

    @DataClass.Generated.Member
    public static final @NonNull Parcelable.Creator<RemoteTaskClientRegistrationInfo> CREATOR
            = new Creator<RemoteTaskClientRegistrationInfo>() {
        @Override
        public RemoteTaskClientRegistrationInfo[] newArray(int size) {
            return new RemoteTaskClientRegistrationInfo[size];
        }

        @Override
        public RemoteTaskClientRegistrationInfo createFromParcel(@NonNull android.os.Parcel in) {
            return new RemoteTaskClientRegistrationInfo(in);
        }
    };

    @DataClass.Generated(
            time = 1679098324017L,
            codegenVersion = "1.0.23",
            sourceFile = "packages/services/Car/car-lib/src/android/car/remoteaccess/RemoteTaskClientRegistrationInfo.java",
            inputSignatures = "private final @android.annotation.NonNull java.lang.String mServiceId\nprivate final @android.annotation.NonNull java.lang.String mVehicleId\nprivate final @android.annotation.NonNull java.lang.String mProcessorId\nprivate final @android.annotation.NonNull java.lang.String mClientId\nclass RemoteTaskClientRegistrationInfo extends java.lang.Object implements [android.os.Parcelable]\n@com.android.car.internal.util.DataClass(genHiddenConstructor=true)")
    @Deprecated
    private void __metadata() {}


    //@formatter:on
    // End of generated code

}
