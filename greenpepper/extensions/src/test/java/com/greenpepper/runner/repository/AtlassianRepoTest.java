package com.greenpepper.runner.repository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.apache.xmlrpc.XmlRpc;
import org.junit.Ignore;
import org.junit.Test;

import com.greenpepper.document.Document;


public class AtlassianRepoTest {

    @Test
    @Ignore
    public void test() throws Exception {
        XmlRpc.debug = true;
        AtlassianRepository repository = new AtlassianRepository("http://localhost/rpc/xmlrpc?handler=greenpepper1&includeStyle=false#TestsGPVenteEncaissement","t0-caisse","passw0rd");
        Document loadDocument = repository.loadDocument("toto?implemented=false");
        System.out.println(loadDocument.getExternalLink());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos);
        loadDocument.print(writer);
        writer.flush();
        System.out.println(baos.toString());
        FileUtils.write(new File("test.html"), baos.toString());
    }

}
