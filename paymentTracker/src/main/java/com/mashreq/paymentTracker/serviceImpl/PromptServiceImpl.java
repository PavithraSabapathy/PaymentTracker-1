package com.mashreq.paymentTracker.serviceImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.mashreq.paymentTracker.constants.ApplicationConstants;
import com.mashreq.paymentTracker.dto.PromptDTO;
import com.mashreq.paymentTracker.dto.PromptResponseDTO;
import com.mashreq.paymentTracker.exception.ResourceNotFoundException;
import com.mashreq.paymentTracker.model.Prompts;
import com.mashreq.paymentTracker.model.Reports;
import com.mashreq.paymentTracker.repository.PromptsRepository;
import com.mashreq.paymentTracker.repository.ReportConfigurationRepository;
import com.mashreq.paymentTracker.service.promptService;

@Component
public class PromptServiceImpl implements promptService {

	@Autowired
	PromptsRepository promptsRepository;

	@Autowired
	ReportConfigurationRepository reportConfigurationRepo;

	@Override
	public List<PromptResponseDTO> fetchAllPrompts() {
		List<PromptResponseDTO> promptResponseDTOList = new ArrayList<PromptResponseDTO>();

		List<Prompts> promptsList = promptsRepository.findAll();
		List<Long> reportId = promptsList.stream().map(Prompts::getReport).map(Prompts -> Prompts.getId())
				.collect(Collectors.toList());

		Map<Reports, List<Prompts>> PromptsReportMap = promptsList.stream()
				.filter(prompts -> reportId.contains(prompts.getReport().getId()))
				.collect(Collectors.groupingBy(Prompts::getReport));

		PromptsReportMap.forEach((reports, promptListMap) -> {
			PromptResponseDTO promptResponseDTO = new PromptResponseDTO();
			List<PromptDTO> promptDTOList = new ArrayList<PromptDTO>();
			promptListMap.forEach(prompts -> {
				PromptDTO promptDTO = new PromptDTO();
				promptDTO.setDisplayName(prompts.getDisplayName());
				promptDTO.setEntityId(prompts.getEntityId());
				promptDTO.setPromptKey(prompts.getPromptKey());
				promptDTO.setPromptOrder(prompts.getPromptOrder());
				promptDTO.setPromptRequired(prompts.getPromptRequired());
				promptDTO.setReportId(prompts.getReport().getId());
				promptDTOList.add(promptDTO);
			});
			promptResponseDTO.setReports(reports);
			promptResponseDTO.setPromptsList(promptDTOList);
			promptResponseDTOList.add(promptResponseDTO);
		});

		return promptResponseDTOList;
	}

	@Override
	public void savePrompt(PromptDTO promptRequest) {
		Prompts promptsObject = new Prompts();
		Optional<Reports> reportOptional = reportConfigurationRepo.findById(promptRequest.getReportId());
		if (reportOptional.isEmpty()) {
			throw new ResourceNotFoundException(
					ApplicationConstants.REPORT_DOES_NOT_EXISTS + promptRequest.getReportId());
		} else {
			Long promptOrderId = promptsRepository.findPromptOrderByReportId(promptRequest.getReportId());
			promptsObject.setPromptOrder(
					promptOrderId != null ? BigInteger.valueOf(promptOrderId).add(BigInteger.ONE) : BigInteger.ONE );
			promptsObject.setDisplayName(promptRequest.getDisplayName());
			promptsObject.setEntityId(null);
			promptsObject.setPromptKey(promptRequest.getPromptKey());
			promptsObject.setPromptRequired(promptRequest.getPromptRequired());
			promptsObject.setReport(reportOptional.get());
			promptsRepository.save(promptsObject);
		}

	}

	@Override
	public void deletePromptById(long promptId) {
		if (promptsRepository.existsById(promptId)) {
			promptsRepository.deleteById(promptId);
		} else {
			throw new ResourceNotFoundException(ApplicationConstants.PROMPTS_DOES_NOT_EXISTS + promptId);
		}
	}

	@Override
	public void updatePromptById(PromptDTO promptRequest, long promptId) {
		Prompts promptsObject = new Prompts();
		Optional<Reports> reportOptional = reportConfigurationRepo.findById(promptRequest.getReportId());
		if (reportOptional.isEmpty()) {
			throw new ResourceNotFoundException(
					ApplicationConstants.REPORT_DOES_NOT_EXISTS + promptRequest.getReportId());
		} else {
			Optional<Prompts> promptResponseOptional = promptsRepository.findById(promptId);
			if (promptResponseOptional.isEmpty()) {
				throw new ResourceNotFoundException(ApplicationConstants.PROMPTS_DOES_NOT_EXISTS + promptId);
			}
			promptsObject.setDisplayName(promptRequest.getDisplayName());
			promptsObject.setEntityId(promptRequest.getEntityId());
			promptsObject.setId(promptId);
			promptsObject.setPromptKey(promptRequest.getPromptKey());
			promptsObject.setPromptOrder(promptRequest.getPromptOrder());
			promptsObject.setPromptRequired(promptRequest.getPromptRequired());
			promptsObject.setReport(reportOptional.get());
			promptsRepository.save(promptsObject);
		}

	}

	@Override
	public List<PromptDTO> fetchPromptsByReportId(long reportId) {
		List<PromptDTO> promptDTOList = new ArrayList<PromptDTO>();
		List<Prompts> promptsListResponse = promptsRepository.findPromptByReportId(reportId);
		if (!CollectionUtils.isEmpty(promptsListResponse)) {
			PromptDTO promptDTO = new PromptDTO();
			promptsListResponse.stream().forEach(promptsResponse -> {
				promptDTO.setDisplayName(promptsResponse.getDisplayName());
				promptDTO.setEntityId(promptsResponse.getEntityId());
				promptDTO.setPromptKey(promptsResponse.getPromptKey());
				promptDTO.setPromptOrder(promptsResponse.getPromptOrder());
				promptDTO.setPromptRequired(promptsResponse.getPromptRequired());
				promptDTO.setReportId(promptsResponse.getReport().getId());
				promptDTOList.add(promptDTO);
			});
		}
		return promptDTOList;

	}

}
