package com.sastix.licenser.server.service;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.sastix.licenser.commons.content.AccessCodeInfoDTO;
import com.sastix.licenser.commons.content.ActivateAccessCodeDTO;
import com.sastix.licenser.commons.content.ImportExcelDTO;
import com.sastix.licenser.commons.content.TenantDTO;
import com.sastix.licenser.commons.exception.AccessCodeNotFoundException;
import com.sastix.licenser.commons.exception.InvalidAccessCodeProvidedException;
import com.sastix.licenser.commons.exception.MalformedExcelException;
import com.sastix.licenser.server.model.AccessCode;
import com.sastix.licenser.server.model.Tenant;
import com.sastix.licenser.server.repository.AccessCodeRepository;
import com.sastix.licenser.server.repository.TenantRepository;
import com.sastix.licenser.server.repository.UserAccessCodeRepository;
import com.sastix.licenser.server.service.impl.LicenserServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static com.sastix.licenser.server.utils.Conversions.convertAccessCodeToDTO;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {LicenserServiceImpl.class})
public class LicenserServiceTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Autowired
    private LicenserService licenserService;

    @MockBean
    private AccessCodeRepository accessCodeRepository;

    @MockBean
    private UserAccessCodeRepository userAccessCodeRepository;

    @MockBean
    private TenantRepository tenantRepository;

    @MockBean
    private ExcelParserService excelParserService;

    private Long userId;

    @Before
    public void setUp() {
        Tenant tenant = new Tenant();
        tenant.setName("Sastix");
        tenant.setParentId(tenant);
        tenantRepository.save(tenant);

        AccessCode code = new AccessCode();
        code.setTenant(tenant);
        code.setAccessCode("test");
        accessCodeRepository.save(code);

        userId = 1L;

        when(accessCodeRepository.getByAccessCode(code.getAccessCode())).thenReturn(code);
        when(tenantRepository.findOne(tenant.getId())).thenReturn(tenant);
        when(tenantRepository.findAll()).thenReturn(new ArrayIterator<>(new Tenant[]{tenant}));
        when(accessCodeRepository.findAll()).thenReturn(new ArrayIterator<>(new AccessCode[]{code}));
    }

    @Test
    public void getAccessCodeInfoTest() {
        String code = "test";
        AccessCode accessCode = accessCodeRepository.getByAccessCode(code);
        AccessCodeInfoDTO accessCodeDTO = licenserService.getAccessCodeInfo(code);
        assertThat(accessCode, is(notNullValue()));
        assertThat(accessCodeDTO.getAccessCode(), equalTo(convertAccessCodeToDTO.apply(accessCode).getAccessCode()));
        assertThat(accessCodeDTO.getDuration(), equalTo(convertAccessCodeToDTO.apply(accessCode).getDuration()));
        assertThat(accessCodeDTO.getLevel(), equalTo(convertAccessCodeToDTO.apply(accessCode).getLevel()));
        assertThat(accessCodeDTO.getActivated(), equalTo(convertAccessCodeToDTO.apply(accessCode).getActivated()));
    }

    @Test
    public void getAccessCodeInfoExceptionTest() {
        expectedEx.expect(AccessCodeNotFoundException.class);
        expectedEx.expectMessage(containsString("cannot be found"));
        licenserService.getAccessCodeInfo("null");
    }

    @Test
    public void activateAccessCodeTest() {
        String code = "test";
        AccessCode accessCode = accessCodeRepository.getByAccessCode(code);
        ActivateAccessCodeDTO activateAccessCodeDTO = new ActivateAccessCodeDTO(code, userId);
        AccessCodeInfoDTO accessCodeDTO = licenserService.activateAccessCode(activateAccessCodeDTO);
        assertThat(accessCode, is(notNullValue()));
        assertThat(accessCode.getIsActivated(), equalTo(true));
        assertThat(accessCodeDTO.getAccessCode(), equalTo(convertAccessCodeToDTO.apply(accessCode).getAccessCode()));
        assertThat(accessCodeDTO.getDuration(), equalTo(convertAccessCodeToDTO.apply(accessCode).getDuration()));
        assertThat(accessCodeDTO.getLevel(), equalTo(convertAccessCodeToDTO.apply(accessCode).getLevel()));
        assertThat(accessCodeDTO.getActivated(), equalTo(convertAccessCodeToDTO.apply(accessCode).getActivated()));
    }

    @Test
    public void activateAccessCodeAccessCodeNotFoundExceptionTest() {
        expectedEx.expect(AccessCodeNotFoundException.class);
        expectedEx.expectMessage(containsString("cannot be found"));
        licenserService.activateAccessCode(new ActivateAccessCodeDTO("null", userId));
    }

    @Test
    public void activateAccessCodeInvalidAccessCodeProvidedExceptionTest() {
        accessCodeRepository.getByAccessCode("test").setIsActivated(true);
        expectedEx.expect(InvalidAccessCodeProvidedException.class);
        expectedEx.expectMessage(containsString("is already activated"));
        licenserService.activateAccessCode(new ActivateAccessCodeDTO("test", 1L));
    }

    @Test
    public void getAllAccessCodesTest(){
        List<AccessCodeInfoDTO> accessCodeInfoDTOS = licenserService.getAllAccessCodes();
        assertThat(accessCodeInfoDTOS.size(), is(1));
        assertThat(accessCodeInfoDTOS.get(0).getAccessCode(), is("test"));
    }

    @Test
    public void importFromExcelTest(){
        reset(excelParserService);
        when(excelParserService.parseFile(any(byte[].class))).thenReturn(Arrays.asList(
                    new AccessCode("test", 0, 1500)
        ));

        List<AccessCodeInfoDTO> accessCodeInfoDTOS = licenserService.importFromExcel(new ImportExcelDTO("test".getBytes(), 0));

        assertThat(accessCodeInfoDTOS.size(), is(1));
        assertThat(accessCodeInfoDTOS.get(0).getAccessCode(), is("test"));
        assertThat(accessCodeInfoDTOS.get(0).getLevel(), is(0));
        assertThat(accessCodeInfoDTOS.get(0).getDuration(), is(1500));
    }

    @Test
    public void importFromExcelExceptionTest(){
        reset(excelParserService);
        when(excelParserService.parseFile(any(byte[].class))).thenThrow(new MalformedExcelException("test"));
        expectedEx.expect(MalformedExcelException.class);
        expectedEx.expectMessage(containsString("test"));

        licenserService.importFromExcel(new ImportExcelDTO("test".getBytes(), 0));
    }

    @Test
    public void getAllTenantsTest(){
        List<TenantDTO> tenantDTOS = licenserService.getAllTenants();
        assertThat(tenantDTOS.size(), is(1));
        assertThat(tenantDTOS.get(0).getName(), is("Sastix"));
    }

}
