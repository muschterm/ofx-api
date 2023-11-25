package muschterm.ofx_api.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import muschterm.ofx_api.dtos.OfxFileDTO;
import muschterm.ofx_api.ofx.OfxHelper;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/ofx")
@Slf4j
public final class OfxResource {

	private final OfxHelper ofxHelper;

	@Inject
	public OfxResource(OfxHelper ofxHelper) {
		this.ofxHelper = ofxHelper;
	}

//	@Post(
//		value = "/file",
//		consumes = MediaType.MULTIPART_FORM_DATA,
//		produces = MediaType.APPLICATION_JSON
//	)
//	public HttpResponse<OfxFileDTO> executeFromFile(CompletedFileUpload ofxFile) throws IOException {

	@GET
	@Path("/file")
	@ResponseStatus(200)
	public OfxFileDTO executeFromFile() throws IOException {
		ofxHelper.handle(
//			ofxFile.getInputStream()
			Files.newInputStream(
				// System.getenv("HOME") + "/Dropbox/Documents/finances/applecard/2020-11.ofx"
				Paths.get(
					System.getenv("HOME"),
					"/Dropbox/Documents/finances/chasecard6126/Chase6126_Activity20190525_20210525_20210525.QFX"
				)
			)
		);

		var ofxFileDTO = new OfxFileDTO();

		return ofxFileDTO;
	}

}
