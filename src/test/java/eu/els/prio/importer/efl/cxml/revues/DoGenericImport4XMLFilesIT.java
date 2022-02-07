package eu.els.prio.importer.efl.cxml.revues;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sie.xd.priorisation.flash.importer.MainTasklet;

/**
 * Change constants below
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:batch-context-test.xml")
@TestPropertySource(properties = {
    "xd.container.host=localhost",
    "xd.job.name=jobTest",
    "gauloisPipeMutiThreadMaxSourceSizeInMO=5",
    "importUrl=http://10.16.14.5:8181/ecm-core/api/import/importXml",
    "gauloisNbrThreads=3",
    "importNbrThreads=7",
    "rootWorkingDir=temp/",
    "makeUnzip=false",
    "makeTransform=true",
    "importFileParamName=xmlFile",
    "maxTime2Wait4Import=30"})
public class DoGenericImport4XMLFilesIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoGenericImport4XMLFilesIT.class);
    private static File tempDir;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @BeforeClass
    public static void beforeClass() {
        // File projectDir = new File(".");
        tempDir = new File("temp/");
        if (tempDir.exists()) {
            LOGGER.info("Removing existing 'temp' dir ");
            tempDir.delete();
            try {
                FileUtils.deleteDirectory(tempDir);
            } catch (IOException e) {
                LOGGER.error("Removing existing 'temp' dir", e);
            }
        }
        tempDir.mkdirs();
    }

    @AfterClass
    public static void afterClass() {
        try {
            FileUtils.deleteDirectory(tempDir);
        } catch (IOException ex) {
        }
    }

    @Test
    public void testXmlFileImport() throws Throwable {
        try {
            Map<String, JobParameter> mapParams = new HashMap<>();
            // TODO
            // nom du fichier à importer pendant le test.
            // ce fichier doit se trouver dans src/test/resources
            String fileName = "SampleUI.cxml.xml";
            String fileNamePrefix = "";
            File dir2ImportFrom = new File(tempDir, "GenericImport");
            dir2ImportFrom.mkdirs();
            File destinationFile = TestUtil.copyFileFromClasspathToDir(this.getClass().getClassLoader(), fileName,
                    fileNamePrefix, dir2ImportFrom);

            mapParams.put(MainTasklet.XDPRIO_LISTENER_FILE_ABSOLUTE_PATH,
                    new JobParameter(destinationFile.getAbsolutePath()));

            JobParameters runtimeParams = new JobParameters(mapParams);

            // Launch !
            JobExecution execution = jobLauncher.run(job, runtimeParams);
            BatchStatus status = execution.getStatus();
            LOGGER.info("Job Exit Status : " + status);
            Assert.assertTrue(status.equals(BatchStatus.COMPLETED));
        } catch (JobExecutionException e) {
            LOGGER.error("Job ExamResult failed", e);
        }
    }

}
