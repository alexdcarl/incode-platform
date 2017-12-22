package org.incode.example.commchannel.dom.impl.postaladdress;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.i18n.TranslatableString;

import org.incode.example.commchannel.dom.CommChannelModule;
import org.incode.example.commchannel.dom.api.GeocodedAddress;
import org.incode.example.commchannel.dom.api.GeocodingService;

@Mixin
public class PostalAddress_update {

    //region > injected services
    @Inject
    GeocodingService geocodingService;
    @Inject
    DomainObjectContainer container;
    //endregion

    //region > mixins
    private PostalAddress_clearGeocode mixinResetGeocode() {
        return container.mixin(PostalAddress_clearGeocode.class, this.postalAddress);
    }
    //endregion

    //region > constructor
    private final PostalAddress postalAddress;
    public PostalAddress_update(final PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    @Programmatic
    public PostalAddress getPostalAddress() {
        return postalAddress;
    }
    //endregion


    public static class DomainEvent extends PostalAddress.ActionDomainEvent<PostalAddress_update> { }
    @Action(
            semantics = SemanticsOf.IDEMPOTENT,
            domainEvent = DomainEvent.class
    )
    public PostalAddress $$(
            @Parameter(maxLength = CommChannelModule.JdoColumnLength.ADDRESS_LINE)
            @ParameterLayout(named = "Address Line 1")
            final String addressLine1,
            @Parameter(maxLength = CommChannelModule.JdoColumnLength.ADDRESS_LINE, optionality = Optionality.OPTIONAL)
            @ParameterLayout(named = "Address Line 2")
            final String addressLine2,
            @Parameter(maxLength = CommChannelModule.JdoColumnLength.ADDRESS_LINE, optionality = Optionality.OPTIONAL)
            @ParameterLayout(named = "Address Line 3")
            final String addressLine3,
            @Parameter(maxLength = CommChannelModule.JdoColumnLength.ADDRESS_LINE, optionality = Optionality.OPTIONAL)
            @ParameterLayout(named = "Address Line 4")
            final String addressLine4,
            @Parameter(maxLength = CommChannelModule.JdoColumnLength.POSTAL_CODE, optionality = Optionality.OPTIONAL)
            @ParameterLayout(named = "Postal Code")
            final String postalCode,
            @Parameter(maxLength = CommChannelModule.JdoColumnLength.COUNTRY, optionality = Optionality.OPTIONAL)
            @ParameterLayout(named = "Country")
            final String country,
            @Parameter(optionality = Optionality.OPTIONAL)
            @ParameterLayout(named = "Lookup geocode")
            final Boolean lookupGeocode) {

        this.postalAddress.setAddressLine1(addressLine1);
        this.postalAddress.setAddressLine2(addressLine2);
        this.postalAddress.setAddressLine3(addressLine3);
        this.postalAddress.setAddressLine4(addressLine4);
        this.postalAddress.setPostalCode(postalCode);
        this.postalAddress.setCountry(country);

        lookupAndUpdateGeocode(
                lookupGeocode,
                addressLine1, addressLine2, addressLine3, addressLine4, postalCode, country);

        return this.postalAddress;
    }

    void lookupAndUpdateGeocode(
            final Boolean lookupGeocode,
            final String... addressParts) {

        if (lookupGeocode == null) {
            return;
        }

        if (lookupGeocode) {
            final String address = geocodingService.combine(GeocodingService.Encoding.ENCODED, addressParts);
            final GeocodedAddress geocodedAddress = geocodingService.lookup(address);

            if (GeocodedAddress.isOk(geocodedAddress)) {
                this.postalAddress.setFormattedAddress(geocodedAddress.getFormattedAddress());
                this.postalAddress.setGeocodeApiResponseAsJson(geocodedAddress.getApiResponseAsJson());
                this.postalAddress.setPlaceId(geocodedAddress.getPlaceId());
                this.postalAddress.setLatLng(geocodedAddress.getLatLng());
                this.postalAddress.setAddressComponents(geocodedAddress.getAddressComponents());
            } else {
                container.warnUser(
                        TranslatableString.tr("Could not lookup geocode for address"),
                        T_addPostalAddress.class, "newPostal");
            }
        } else {
            mixinResetGeocode().$$();
        }
    }

    public String default0$$() {
        return this.postalAddress.getAddressLine1();
    }
    public String default1$$() {
        return this.postalAddress.getAddressLine2();
    }
    public String default2$$() {
        return this.postalAddress.getAddressLine3();
    }
    public String default3$$() {
        return this.postalAddress.getAddressLine4();
    }
    public String default4$$() {
        return this.postalAddress.getPostalCode();
    }
    public String default5$$() {
        return this.postalAddress.getCountry();
    }
    public Boolean default6$$() {
        return this.postalAddress.getPlaceId() != null ? true: null;
    }


}
