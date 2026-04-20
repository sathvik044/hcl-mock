package com.example.CorporateTravelManagementSystem.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.CorporateTravelManagementSystem.Repository.ItineraryRepository;
import com.example.CorporateTravelManagementSystem.Repository.TravelRequestRepository;
import com.example.CorporateTravelManagementSystem.dto.ItineraryRequestDto;
import com.example.CorporateTravelManagementSystem.dto.ItineraryResponseDto;
import com.example.CorporateTravelManagementSystem.entity.Itinerary;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;
import com.example.CorporateTravelManagementSystem.enums.ItineraryStatus;
import com.example.CorporateTravelManagementSystem.exception.BadRequestException;
import com.example.CorporateTravelManagementSystem.exception.ResourceNotFoundException;
import com.example.CorporateTravelManagementSystem.mapper.ItineraryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TravelRequestRepository travelRequestRepository;

    
    public ItineraryResponseDto create(ItineraryRequestDto dto) {
        validateCreateRequest(dto);

        TravelRequestEntity travelRequest = travelRequestRepository.findById(dto.getTravelRequestId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Travel request not found for id: " + dto.getTravelRequestId()));

        Itinerary itinerary = ItineraryMapper.toEntity(dto, travelRequest);

        Itinerary saved = itineraryRepository.save(itinerary);

        return ItineraryMapper.toDTO(saved);
    }

    public List<ItineraryResponseDto> getByTravelRequest(Long requestId) {

        List<Itinerary> itineraries = itineraryRepository.findByTravelRequestId(requestId);

        return itineraries.stream()
                .map(ItineraryMapper::toDTO)
                .collect(Collectors.toList());
    }
    public ItineraryResponseDto confirm(Long id) {

        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary not found for id: " + id));

        if (itinerary.getStatus() != ItineraryStatus.PENDING) {
            throw new BadRequestException("Only pending itineraries can be confirmed");
        }

        itinerary.setStatus(ItineraryStatus.CONFIRMED);

        Itinerary updated = itineraryRepository.save(itinerary);

        return ItineraryMapper.toDTO(updated);
    }

    private void validateCreateRequest(ItineraryRequestDto dto) {
        if (dto == null) {
            throw new BadRequestException("Request body is required");
        }
        if (dto.getTravelRequestId() == null) {
            throw new BadRequestException("travelRequestId is required");
        }
        if (dto.getSegmentType() == null) {
            throw new BadRequestException("segmentType is required");
        }
        if (dto.getDepartureDateTime() == null || dto.getArrivalDateTime() == null) {
            throw new BadRequestException("departureDateTime and arrivalDateTime are required");
        }
        if (!dto.getArrivalDateTime().isAfter(dto.getDepartureDateTime())) {
            throw new BadRequestException("arrivalDateTime must be after departureDateTime");
        }
        if (dto.getFromLocation() == null || dto.getFromLocation().isBlank()
                || dto.getToLocation() == null || dto.getToLocation().isBlank()) {
            throw new BadRequestException("fromLocation and toLocation are required");
        }
        if (dto.getFromLocation().trim().equalsIgnoreCase(dto.getToLocation().trim())) {
            throw new BadRequestException("fromLocation and toLocation cannot be the same");
        }
        if (dto.getCost() == null || dto.getCost() <= 0) {
            throw new BadRequestException("cost must be greater than 0");
        }
    }
}
