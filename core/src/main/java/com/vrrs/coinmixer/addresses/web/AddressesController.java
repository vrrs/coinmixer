package com.vrrs.coinmixer.addresses.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.vrrs.coinmixer.addresses.domain.MixedAddressesWithBalances;
import com.vrrs.coinmixer.addresses.domain.SourceAddresses;
import com.vrrs.coinmixer.addresses.domain.WithdrawalTransactionStatus;
import com.vrrs.coinmixer.addresses.services.AddressesServiceFacade;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/coinmixer/v1")
public final class AddressesController {
	
	private final AddressesServiceFacade addressesServiceFacade;

	public AddressesController(AddressesServiceFacade addressesServiceFacade) {
		this.addressesServiceFacade = addressesServiceFacade;
	}
	
	@POST
	@Path("/addresses")
	public MixedAddressesWithBalances getNewMixedAddressesFrom(SourceAddresses sourceAddresses) {
		return addressesServiceFacade.getNewMixedAddresses(sourceAddresses.getSourceAddresses());
	}
	
	@GET
	@Path("/addresses/{address}")
	public MixedAddressesWithBalances getMixedAddresses(@PathParam("address") String address) {
		return addressesServiceFacade.getMixedAddresses(address);
	}
	
	@POST
	@Path("/withdrawal/{address}")
	public WithdrawalTransactionStatus initiateWithdraw(@PathParam("address") String address) {
		return addressesServiceFacade.initiateWithdraw(address);
	}
	
	@POST
	@Path("/withdrawal/")
	public MixedAddressesWithBalances initiateWithdraw(SourceAddresses sourceAddresses) {
		return addressesServiceFacade.getNewMixedAddressesAndWithdraw(sourceAddresses.getSourceAddresses());
	}

}
