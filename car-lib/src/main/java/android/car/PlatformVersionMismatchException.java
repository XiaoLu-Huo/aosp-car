/*
 * Copyright (C) 2022 The Android Open Source Project
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
package android.car;

import static com.android.car.internal.ExcludeFromCodeCoverageGeneratedReport.BOILERPLATE_CODE;

import android.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.car.internal.ExcludeFromCodeCoverageGeneratedReport;
import com.android.car.internal.util.AnnotationValidations;
import com.android.car.internal.util.DataClass;

/**
 * Exception thrown when an App tries to calls an API not supported in the platform version.
 *
 * <p>Apps are expected to check the {@code ApiRequirements} for each API. If the API is
 * not supported for the current platform, the API should not be called. Apps can use
 * {@link Car#getPlatformVersion()} to get the current platform version.
 */
@DataClass()
public final class PlatformVersionMismatchException extends UnsupportedOperationException
        implements Parcelable {

    @NonNull
    private final PlatformVersion mExpectedPlatformApiVersion;

    @Override
    @NonNull
    public String getMessage() {
        return "Expected version: "
                + mExpectedPlatformApiVersion + ", Current version: " + Car.getPlatformVersion();
    }


    // Code below generated by codegen v1.0.23.
    //
    // DO NOT MODIFY!
    // CHECKSTYLE:OFF Generated code
    //
    // To regenerate run:
    // $ codegen $ANDROID_BUILD_TOP/packages/services/Car/car-lib/src/android/car/PlatformVersionMismatchException.java
    // Added AddedInOrBefore or ApiRequirement Annotation manually
    //
    // To exclude the generated code from IntelliJ auto-formatting enable (one-time):
    //   Settings > Editor > Code Style > Formatter Control
    //@formatter:off


    @DataClass.Generated.Member
    public PlatformVersionMismatchException(
            @NonNull PlatformVersion expectedPlatformApiVersion) {
        this.mExpectedPlatformApiVersion = expectedPlatformApiVersion;
        AnnotationValidations.validate(
                NonNull.class, null, mExpectedPlatformApiVersion);

        // onConstructed(); // You can define this method to get a callback
    }

    /**
     * Gets expected platform API version.
     */
    @DataClass.Generated.Member
    public @NonNull PlatformVersion getMinimumPlatformApiVersion() {
        return mExpectedPlatformApiVersion;
    }

    @Override
    @DataClass.Generated.Member
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        // You can override field parcelling by defining methods like:
        // void parcelFieldName(Parcel dest, int flags) { ... }

        dest.writeTypedObject(mExpectedPlatformApiVersion, flags);
    }

    @Override
    @DataClass.Generated.Member
    @ExcludeFromCodeCoverageGeneratedReport(reason = BOILERPLATE_CODE)
    public int describeContents() { return 0; }

    /** @hide */
    @SuppressWarnings({"unchecked", "RedundantCast"})
    @DataClass.Generated.Member
    /* package-private */ PlatformVersionMismatchException(@NonNull Parcel in) {
        // You can override field unparcelling by defining methods like:
        // static FieldType unparcelFieldName(Parcel in) { ... }

        PlatformVersion expectedPlatformApiVersion = (PlatformVersion) in
                .readTypedObject(PlatformVersion.CREATOR);

        this.mExpectedPlatformApiVersion = expectedPlatformApiVersion;
        AnnotationValidations.validate(
                NonNull.class, null, mExpectedPlatformApiVersion);

        // onConstructed(); // You can define this method to get a callback
    }

    @DataClass.Generated.Member
    public static final @NonNull Parcelable.Creator<PlatformVersionMismatchException> CREATOR
            = new Creator<PlatformVersionMismatchException>() {
        @Override
        public PlatformVersionMismatchException[] newArray(int size) {
            return new PlatformVersionMismatchException[size];
        }

        @Override
        public PlatformVersionMismatchException createFromParcel(@NonNull Parcel in) {
            return new PlatformVersionMismatchException(in);
        }
    };

    @DataClass.Generated(
            time = 1658181489415L,
            codegenVersion = "1.0.23",
            sourceFile = "packages/services/Car/car-lib/src/android/car/PlatformVersionMismatchException.java",
            inputSignatures = "private final @android.annotation.NonNull android.car.PlatformApiVersion mExpectedPlatformApiVersion\npublic @java.lang.Override @android.annotation.NonNull @android.car.annotation.AddedIn java.lang.String getMessage()\nclass PlatformVersionMismatchException extends java.lang.UnsupportedOperationException implements [android.os.Parcelable]\n@com.android.car.internal.util.DataClass()")
    @Deprecated
    private void __metadata() {}


    //@formatter:on
    // End of generated code

}
