/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.impl.FileServiceImpl;
import edu.yu.einstein.wasp.service.impl.UserServiceImpl;

/**
 * @author calder
 * 
 */
@ContextConfiguration(locations = { "classpath:/META-INF/application-context-sge-test.xml" })
public class IntegratedSgeServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    GridTransportConnection testGridTransportConnection;

    @Autowired
    @Qualifier("testGridWorkService")
    GridWorkService gws;

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private DataSource testDataSource;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public void beforeClass() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, SQLException {

    }

    @Test(groups = { "ssh" })
    @Transactional("entityManager")
    public void testFile() throws InterruptedException, GridException, FileNotFoundException, URISyntaxException, SQLException {

        WorkUnit test = new WorkUnit(new WorkUnitGridConfiguration());
        test.getConfiguration().setResultsDirectory(WorkUnitGridConfiguration.RESULTS_DIR_PLACEHOLDER + "/test");
        FileGroup fg = new FileGroup();
        FileHandle fh = new FileHandle();
        fh.setFileName("test.txt");

        fg.addFileHandle(fh);

        fh = fileService.addFile(fh);
        fg = fileService.addFileGroup(fg);

        logger.debug("fileHandle ID: " + fh.getId());

        LinkedHashSet<FileGroup> rs = new LinkedHashSet<FileGroup>();
        rs.add(fg);
        test.addResultFile(fh);
        test.addCommand("echo \"this is a test\" > ${" + WorkUnit.OUTPUT_FILE + "[0]}");

        GridResult r = gws.execute(test);

        while (!gws.isFinished(r)) {
            Thread.sleep(4000);
            logger.debug("waiting for test");
        }

        logger.debug("REGISTERED MD5: " + fileService.getFileHandleById(fh.getId()).getMd5hash());

    }

    @Test(groups = { "ssh" })
    @Transactional("entityManager")
    public void testMD5() throws GridException, InterruptedException {
    	WorkUnit test = new WorkUnit(new WorkUnitGridConfiguration());
        test.getConfiguration().setResultsDirectory(WorkUnitGridConfiguration.RESULTS_DIR_PLACEHOLDER + "/test");
        FileGroup fg = new FileGroup();
        FileHandle fh;

        LinkedHashSet<FileHandle> fileHandles = new LinkedHashSet<FileHandle>();

        int numFiles = 10;

        for (int i = 0; i < numFiles; i++) {
            fh = new FileHandle();
            fh.setFileName("test-" + i + ".txt");
            fileHandles.add(fh);
            fileService.addFile(fh);
            test.addCommand("echo \"this is a test: \" " + i + " > ${" + WorkUnit.OUTPUT_FILE + "[" + i + "]}");
        }
        fg.setFileHandles(fileHandles);
        fileService.addFileGroup(fg);

        test.setResultFiles(fileHandles);

        GridResult r = gws.execute(test);

        while (!gws.isFinished(r)) {
            Thread.sleep(4000);
            logger.debug("waiting for test");
        }

        String[] sums = { "bf4d91ae94d85f84bd7f99d7da5145cc", "f46b0d79a741c296ce76bb6fe275dad4", "74df7b2759fd8c8526a65000356e3235",
                "0620a062fc340deb9b8b44b4d5effbe7", "6cacf7bcf0782c2e1fd3ba98b0243938", "bc6380f9f14728563e4b7a2c192bcaf7", "717a8e358c04d230ee4e3b7b9abc83cd",
                "7460575307a4a20c50744c1871ba4bf9", "ce9f6ae9bbba9b930d66b7a469e1f374", "ed2ed5b716844541934cdc237d1e4eb1", "74eddf53f0928a434f4c738d8f68a33b",
                "f2405f3276a56c362bdd0711355dea2e", "d493c3c955f5eb1fa4c75b7c66e13f07", "10841decb7ed08f0a515ee5bfa54e80c", "ebd6e2361fa11d57ed67465776cbba27",
                "1020ca7c994ad6820b71f97aa77459a1", "597735c282a01076ed911fe80e54864e", "e06804664b2f47a999e40c3af46d4077", "19b9804ef3dca4598155393a2b1172da",
                "c655b5ba47d7d8217fc4597bb1867f59", "faa56d48ffabc96c44a061f323758924", "70f204ac3fc2569500de2f1ecd3d7750", "10fb5deada15b5258e0bf562aef713bb",
                "d128fb3ac2a3fee4c5e712a4b8c46e4f", "14373ed09ba85d8429ec770e09ec1c6a", "64607640c99fd7f37c90117c7db70758", "1639be9c4b717fa4193e8967a88b67e6",
                "02caad5da41569efd2d081502fabc2d5", "ca9663e4bb99a11b272d2313c05a19b1", "23221ce8cf17b43cd5af5a7aab4341c0", "542816937f4ea77c3f4057e618bdfb37",
                "f327d60db9b5679b747dcb5006a008c9", "332e456167ff56ee0fc295f4ec405a98", "ee1a1b66640cbfd4652663d045e8887a", "41daf1e98a4b68b7e7e0c62687157372",
                "ebbf4bb3c9029947925bc4d22e75d4f5", "cd8a43e5c6844a0f96df5e820b146b8a", "3e5242f0e1864535ee038497bff248fd", "cf8af8265965506a8e1f9b3382659347",
                "34d388da8a2ace67638d43ec68306382", "29d87940670a06e4c47015171886eca5", "f3893c5c9006513eefe1f2c970108b0b", "97b6db58a5440a91328ca0936cd00320",
                "2ca5b1f0b634bd1ab37bdf7cab68326f", "37d4581128eb8a1cff3f8c4f6df7837f", "f3179a23d1cd666fdea4362b321195b1", "336737b5e61b6ff644941ab630563b8c",
                "ac1642a5acdf300e01fb3b13dea4c49c", "64363e5c1048b6f6101d740fa0e3595e", "4b07e365a832907f5164758b34e2d06e", "639f80a243decaecf81fa4dde66c6555",
                "86bd4aac58b602ffb6607b12aa87252f", "7d13477d0cb715e3864ddfca88012352", "912cab71f67587e6d75ce6354dca0321", "890d7ef0739fec8962075f24f22476ba",
                "c223d5b80d8a025a8e4eee46dce0cb81", "048fc4dfe030afb2440cba03014e0d29", "cb70d757d8a8c4a0599d1eedaaa0d3ae", "6f5b1ae180754b2ea339529b99b76d72",
                "8aef0eeebaf2df3035fcd9c849cc0422", "c792da7cf9340f68bc42da74608a5dbf", "7229a3b13cff07cc14b1b710630b7665", "203ba1ac34ba94119c17c821b95690e5",
                "d10f2f65358d607e3a94942d86903d8e", "3ed0b5a979371e57476e6acf14808e43", "24e9662db089b65ecbade8fdda9b70c4", "2a685d424f2516dd5cacd2f502b759a9",
                "a81f5b8980d9247a7030a88059826d24", "eb53f30b710df44e3330f89c7b42b89d", "13fe5deef315082bfeedc860c6b44054", "177a82a08433ccc2c5a807fd01623726",
                "4974d379fc02e3c1942f2718098b443f", "e9edfe49ea6425bd765c52f203df5319", "b0bd4831a2e13bfd3003e213ac6d203e", "a6f3da7205c5d82f07454ccf58e613d7",
                "2240ef6d99517be5ec17467e58ffeb63", "99514b1c1a2e1c3a6620542db3592912", "cc78e5286a1405aee7e1a261bbce833a", "0472ed8e36e160d20d95b2626173cfc6",
                "cbbf74e1f3f59238f6d55dab66c36183", "01c9e17fec8ddb101b7a5e4e45c4ea84", "6562a77853a607128b472e921aeb2b43", "f212185f2106413719f3684db9986d9f",
                "d4283fd17b71b5e56fdb24fa5fef8c07", "6510bf942d99f2ca43ad1c5e8d4a97b8", "9385f04887c35c3a6fbaf8dbbcd9761c", "50da236f811b8cd5f13316ec34aacf9f",
                "dbd83eb9157ba0810895bcb91b3d3783", "4e5872d8748b112f6d96e9d76d2f4124", "98e6f64a9b77f0f22c41cb8e7a5d0839", "da43ece1d481fc19c8f4839bc7b7c0de",
                "026631ea94354448ec7c134c3a115d06", "0455ee87521f1c485535f63f92ce687a", "ace6a8bd56b8d12b3a403d13acc6b118", "1ed80cec01b23206b66f21f9539b4461",
                "58b9f008dd3c658f018ab27b39316703", "11c68849c54c3ec8176b0cf50a1bea40", "cb60972d0313083d31c207f155a122da", "fd08ab1a760cf6b944612ebf28d78d4f",
                "e516b38b42dc968d0f95c36cec30567f" };

        List<Integer> fids = r.getFileHandleIds();

        for (int i = 0; i < numFiles; i++) {
            FileHandle f = fileService.getFileHandleById(fids.get(i));
            logger.debug("MD5: " + f.getMd5hash());
            Assert.assertTrue(f.getMd5hash().equals(sums[i]));
        }

    }

}
