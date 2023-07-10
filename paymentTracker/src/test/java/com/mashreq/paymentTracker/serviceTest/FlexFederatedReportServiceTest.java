package com.mashreq.paymentTracker.serviceTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mashreq.paymentTracker.dto.AdvanceSearchReportInput;
import com.mashreq.paymentTracker.dto.LinkedReportResponseDTO;
import com.mashreq.paymentTracker.dto.ReportContext;
import com.mashreq.paymentTracker.dto.ReportExecuteResponseData;
import com.mashreq.paymentTracker.dto.ReportInstanceDTO;
import com.mashreq.paymentTracker.dto.ReportPromptsInstanceDTO;
import com.mashreq.paymentTracker.exception.ResourceNotFoundException;
import com.mashreq.paymentTracker.model.Components;
import com.mashreq.paymentTracker.model.ComponentsCountry;
import com.mashreq.paymentTracker.model.Report;
import com.mashreq.paymentTracker.repository.ComponentsCountryRepository;
import com.mashreq.paymentTracker.repository.ComponentsRepository;
import com.mashreq.paymentTracker.service.CannedReportService;
import com.mashreq.paymentTracker.service.LinkReportService;
import com.mashreq.paymentTracker.service.QueryExecutorService;
import com.mashreq.paymentTracker.service.ReportConfigurationService;
import com.mashreq.paymentTracker.serviceImpl.FlexFederatedReportServiceImpl;
import com.mashreq.paymentTracker.type.CountryType;

@ContextConfiguration(classes = { FlexFederatedReportServiceImpl.class })
@ExtendWith(SpringExtension.class)
class FlexFederatedReportServiceTest {
	@MockBean
	private CannedReportService cannedReportService;

	@MockBean
	private ComponentsCountryRepository componentsCountryRepository;

	@MockBean
	private ComponentsRepository componentsRepository;

	@Autowired
	private FlexFederatedReportServiceImpl flexFederatedReportServiceImpl;

	@MockBean
	private LinkReportService linkReportService;

	@MockBean
	private QueryExecutorService queryExecutorService;

	@MockBean
	private ReportConfigurationService reportConfigurationService;

	@Test
    void testProcessFlexReport() {
             
        ReportInstanceDTO reportInstanceDTO = new ReportInstanceDTO();
        reportInstanceDTO
                .setCreationDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        reportInstanceDTO.setId(1L);
        reportInstanceDTO.setModuleId(1L);
        reportInstanceDTO.setPromptsList(new ArrayList<>());
        reportInstanceDTO.setReportId(1L);
        reportInstanceDTO.setReportInstanceComponents(new HashSet<>());
        reportInstanceDTO.setReportInstanceMetrics(new HashSet<>());
        reportInstanceDTO.setReportInstancePrompts(new HashSet<>());
        reportInstanceDTO.setReportName("Report Name");
        reportInstanceDTO.setRoleId(1L);
        reportInstanceDTO.setRoleName("Role Name");
        reportInstanceDTO.setUserId(1L);
        reportInstanceDTO.setUserName("janedoe");

        ReportInstanceDTO reportInstance = new ReportInstanceDTO();
        reportInstance
                .setCreationDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        reportInstance.setId(1L);
        reportInstance.setModuleId(1L);
        reportInstance.setPromptsList(new ArrayList<>());
        reportInstance.setReportId(1L);
        reportInstance.setReportInstanceComponents(new HashSet<>());
        reportInstance.setReportInstanceMetrics(new HashSet<>());
        reportInstance.setReportInstancePrompts(new HashSet<>());
        reportInstance.setReportName("Report Name");
        reportInstance.setRoleId(1L);
        reportInstance.setRoleName("Role Name");
        reportInstance.setUserId(1L);
        reportInstance.setUserName("janedoe");

        ReportContext reportContext = new ReportContext();
        reportContext.setCountry(CountryType.UAE);
        reportContext.setExecutionId(1L);
        reportContext.setLinkInstanceId(1L);
        reportContext.setLinkReference("Link Reference");
        reportContext.setLinkedReport(true);
        reportContext.setModuleId(1L);
        reportContext.setReportId(1L);
        reportContext.setReportInstance(reportInstance);
        reportContext.setReportName("Report Name");
        reportContext.setRoleId(1L);
        reportContext.setRoleName("Role Name");
        reportContext.setUserId(1L);
        reportContext.setUserName("janedoe");
      
        Report report = new Report();
        report.setId(1L);
        
        ArrayList<Components> componentsList = new ArrayList<>();
        componentsList.add(new Components());
        Optional<List<Components>> ofResult = Optional.of(componentsList);


        ArrayList<LinkedReportResponseDTO> linkedReportResponseDTOList = new ArrayList<>();
        linkedReportResponseDTOList.add(new LinkedReportResponseDTO(1L, "Link Name", "Link Description", "Report Name",
                0, 0, "Linked Report Name", "Source Metric Name", 0, "Active", null, null));
       
        
        when(reportConfigurationService.fetchReportByName(Mockito.<String>any())).thenReturn(new Report());
        when(linkReportService.fetchLinkedReportByReportId(anyLong())).thenReturn(linkedReportResponseDTOList);
        when(componentsRepository.findAllByreportId(anyLong())).thenReturn(ofResult);   

     
        ReportExecuteResponseData actualProcessFlexReportResult = flexFederatedReportServiceImpl
                .processFlexReport(reportInstanceDTO, reportContext);
        
        assertNotNull(actualProcessFlexReportResult);

		assertEquals("Report Name", actualProcessFlexReportResult.getMeta().getReportId());
		
        verify(componentsRepository).findAllByreportId(anyLong());
        verify(linkReportService).fetchLinkedReportByReportId(anyLong());
        verify(reportConfigurationService).fetchReportByName(Mockito.<String>any());
    }


	@Test
	void testProcessFlexDetailReport() {
			FlexFederatedReportServiceImpl flexFederatedReportServiceImpl = new FlexFederatedReportServiceImpl();
		AdvanceSearchReportInput advanceSearchReportInput = mock(AdvanceSearchReportInput.class);
		Components components = mock(Components.class);
		when(components.getActive()).thenThrow(new ResourceNotFoundException("An error occurred"));
		when(components.getComponentKey()).thenReturn("flex");

		ArrayList<Components> componentList = new ArrayList<>();
		componentList.add(components);
		assertThrows(ResourceNotFoundException.class, () -> flexFederatedReportServiceImpl
				.processFlexDetailReport(advanceSearchReportInput, componentList, mock(ReportContext.class)));
		verify(components).getActive();
		verify(components).getComponentKey();
	}

	@Test
	void testProcessFlexDetailReport2() {
	
		FlexFederatedReportServiceImpl flexFederatedReportServiceImpl = new FlexFederatedReportServiceImpl();
		AdvanceSearchReportInput advanceSearchReportInput = mock(AdvanceSearchReportInput.class);
		Components components = mock(Components.class);
		when(components.getActive()).thenThrow(new ResourceNotFoundException("An error occurred"));
		when(components.getComponentKey()).thenReturn("flex");

		ArrayList<Components> componentList = new ArrayList<>();
		Report report = new Report();
		ComponentsCountry componentsCountry = new ComponentsCountry();
		componentList.add(new Components(1L, "Component Name", "Component Key", "Active", report, componentsCountry,
				new ArrayList<>()));
		componentList.add(components);
		assertThrows(ResourceNotFoundException.class, () -> flexFederatedReportServiceImpl
				.processFlexDetailReport(advanceSearchReportInput, componentList, mock(ReportContext.class)));
		verify(components).getActive();
		verify(components).getComponentKey();
	}
}
