package com.example.CorporateTravelManagementSystem.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.CorporateTravelManagementSystem.Repository.ItineraryRepository;
import com.example.CorporateTravelManagementSystem.Repository.TravelRequestRepository;
import com.example.CorporateTravelManagementSystem.dto.ItineraryRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.ItineraryResponseDTO;
import com.example.CorporateTravelManagementSystem.entity.Itinerary;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;
import com.example.CorporateTravelManagementSystem.enums.ItineraryStatus;
import com.example.CorporateTravelManagementSystem.mapper.ItineraryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TravelRequestRepository travelRequestRepository;

    
    public ItineraryResponseDTO create(ItineraryRequestDTO dto) {

        TravelRequestEntity travelRequest = travelRequestRepository.findById(dto.getTravelRequestId())
                .orElseThrow(() -> new RuntimeException("Travel Request not found"));

        Itinerary itinerary = ItineraryMapper.toEntity(dto, travelRequest);

        Itinerary saved = itineraryRepository.save(itinerary);

        return ItineraryMapper.toDTO(saved);
    }

    public List<ItineraryResponseDTO> getByTravelRequest(Long requestId) {

        List<Itinerary> itineraries = itineraryRepository.findByTravelRequestId(requestId);

        return itineraries.stream()
                .map(ItineraryMapper::toDTO)
                .collect(Collectors.toList());
    }
    public ItineraryResponseDTO confirm(Long id) {

        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        if (itinerary.getStatus() != ItineraryStatus.PENDING) {
            throw new RuntimeException("Only pending itineraries can be confirmed");
        }

        itinerary.setStatus(ItineraryStatus.CONFIRMED);

        Itinerary updated = itineraryRepository.save(itinerary);

        return ItineraryMapper.toDTO(updated);
    }
}
