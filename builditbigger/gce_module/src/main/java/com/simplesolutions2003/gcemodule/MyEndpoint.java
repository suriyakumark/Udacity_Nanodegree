/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.simplesolutions2003.gcemodule;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import java.util.logging.Logger;
import javax.inject.Named;
import com.simplesolutions2003.jokesFactory.JokesSupplier;


/** An endpoint class we are exposing */
@Api(
  name = "jokesApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "gcemodule.simplesolutions2003.com",
    ownerName = "gcemodule.simplesolutions2003.com",
    packagePath=""
  )
)
public class MyEndpoint {
    private static final Logger log = Logger.getLogger(MyEndpoint.class.getName());

    @ApiMethod(name = "getJoke")
    public MyBean getJoke(@Named("Joke") String name) {
        log.info("getJoke");
        MyBean response = new MyBean();

        //get a joke from java library
        JokesSupplier jokes = new JokesSupplier();
        response.setData(jokes.getJoke());

        log.info("response"+response);
        return response;
    }

}
