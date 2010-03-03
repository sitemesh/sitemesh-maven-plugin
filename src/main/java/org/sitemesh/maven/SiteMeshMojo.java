package org.sitemesh.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.model.fileset.util.FileSetManager;
import org.apache.maven.shared.model.fileset.FileSet;
import org.sitemesh.builder.SiteMeshOfflineBuilder;
import org.sitemesh.config.xml.XmlOfflineConfigurator;
import org.sitemesh.config.ObjectFactory;
import org.sitemesh.offline.SiteMeshOffline;
import org.w3c.dom.Element;

import static javax.xml.parsers.DocumentBuilderFactory.newInstance;
import java.io.File;
import java.io.IOException;

/**
 * A SiteMesh Maven Mojo for version 3.x
 *
 * @author Richard L. Burton III - SmartCode LLC
 * @goal sitemesh
 */
public class SiteMeshMojo extends AbstractMojo {

    /**
     * The destination directory where the decorated files will be placed.
     *
     * @parameter expression="${basedir}" default-value="${basedir}"
     * @required
     */
    private File destDir;

    /**
     * The sitemesh configuration file.
     *
     * @parameter expression="${config}"
     */
    private File config;

    /**
     * @parameter
     */
    private FileSet fileset;

    public void execute() throws MojoExecutionException {
        SiteMeshOffline siteMeshOffline = createSiteMeshOffline();
        FileSetManager filesetManager = new FileSetManager();
        String[] files = filesetManager.getIncludedFiles(fileset);
        try {
            for (String file : files) {
                siteMeshOffline.process(file);
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }

    protected SiteMeshOffline createSiteMeshOffline() throws MojoExecutionException {
        SiteMeshOfflineBuilder builder = new SiteMeshOfflineBuilder();
        if (config != null) {
            XmlOfflineConfigurator offlineConfigurator = new XmlOfflineConfigurator(new ObjectFactory.Default(), parseSiteMeshXmlConfig(config));
            offlineConfigurator.configureOffline(builder);
        }

        return builder.setDestinationDirectory(destDir)
                .setSourceDirectory(fileset.getDirectory())
                .create();
    }


    protected Element parseSiteMeshXmlConfig(File config) throws MojoExecutionException {
        try {
            return newInstance().newDocumentBuilder().parse(config).getDocumentElement();
        } catch (Exception e) {
            throw new MojoExecutionException("Could not parse " + config.getAbsolutePath() + " : " + e.getMessage(), e);
        }
    }

    public File getDestDir() {
        return destDir;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public File getConfig() {
        return config;
    }

    public void setConfig(File config) {
        this.config = config;
    }

    public void setFileset(FileSet fileset) {
        this.fileset = fileset;
    }
}

