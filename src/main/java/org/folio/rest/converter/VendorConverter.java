package org.folio.rest.converter;

import org.folio.rest.jaxrs.model.*;
import org.folio.rest.util.RestConstants;
import org.folio.rmapi.model.Vendor;
import org.folio.rmapi.model.VendorById;
import org.folio.rmapi.model.Vendors;

import java.util.List;
import java.util.stream.Collectors;

public class VendorConverter {

  private static final Relationships EMPTY_PACKAGE_RELATIONSHIP = new Relationships()
    .withPackages(new Packages()
      .withMeta(new MetaDataIncluded()
        .withIncluded(false))
      .withData(null));
  private final String PROVIDERS_TYPE = "providers";

  public ProviderCollection convert(Vendors vendors) {
    List<Providers> providerList = vendors.getVendorList().stream()
      .map(this::convertVendor)
      .collect(Collectors.toList());
    return new ProviderCollection()
      .withJsonapi(RestConstants.JSONAPI)
      .withMeta(new MetaTotalResults().withTotalResults(vendors.getTotalResults()))
      .withData(providerList);
  }

  private Providers convertVendor(Vendor vendor) {
    String token = vendor.getVendorToken();
    return new Providers()
      .withId(String.valueOf(vendor.getVendorId()))
      .withType(PROVIDERS_TYPE)
      .withAttributes(new ProviderListDataAttributes()
        .withName(vendor.getVendorName())
        .withPackagesTotal(vendor.getPackagesTotal())
        .withPackagesSelected(vendor.getPackagesSelected())
        .withSupportsCustomPackages(vendor.isCustomer())
        .withProviderToken(token != null ? new Token().withValue(token) : null))
      .withRelationships(EMPTY_PACKAGE_RELATIONSHIP);
  }

  public Provider convertToVendor(VendorById vendor) {
    String token = vendor.getVendorToken();
    return new Provider()
      .withData(new ProviderData()
        .withId(String.valueOf(vendor.getVendorId()))
        .withType(PROVIDERS_TYPE)
        .withAttributes(new ProviderDataAttributes()
          .withName(vendor.getVendorName())
          .withPackagesTotal(vendor.getPackagesTotal())
          .withPackagesSelected(vendor.getPackagesSelected())
          .withSupportsCustomPackages(vendor.isCustomer())
          .withProviderToken(token != null ? new Token().withValue(token) : null)
          .withProxy(new Proxy()
            .withId(vendor.getProxy().getId())
            .withInherited(vendor.getProxy().getInherited()))
        )
        .withRelationships(EMPTY_PACKAGE_RELATIONSHIP))
      .withIncluded(null)
      .withJsonapi(RestConstants.JSONAPI);
  }
}
