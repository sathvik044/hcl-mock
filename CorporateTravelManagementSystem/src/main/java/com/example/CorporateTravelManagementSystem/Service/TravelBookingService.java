package com.example.CorporateTravelManagementSystem.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.CorporateTravelManagementSystem.Repository.ItineraryRepository;
import com.example.CorporateTravelManagementSystem.Repository.TravelBookingRepository;
import com.example.CorporateTravelManagementSystem.Repository.TravelRequestRepository;
import com.example.CorporateTravelManagementSystem.Repository.UserRepository;
import com.example.CorporateTravelManagementSystem.dto.TravelBookingRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelBookingResponseDTO;
import com.example.CorporateTravelManagementSystem.entity.Itinerary;
import com.example.CorporateTravelManagementSystem.entity.TravelBooking;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.mapper.TravelBookingMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelBookingService {

    private final TravelBookingRepository bookingRepository;
    private final TravelRequestRepository travelRequestRepository;
    private final ItineraryRepository itineraryRepository;
    private final UserRepository userRepository;

   
    public TravelBookingResponseDTO create(TravelBookingRequestDTO dto) {

        TravelRequestEntity request = travelRequestRepository.findById(dto.getTravelRequestId())
                .orElseThrow(() -> new RuntimeException("Travel request not found"));

        Itinerary itinerary = itineraryRepository.findById(dto.getItineraryId())
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        User user = userRepository.findById(dto.getBookedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getStatus() == null || !request.getStatus().name().equals("FINANCE_APPROVED")) {
            throw new RuntimeException("Booking allowed only after finance approval");
        }

        TravelBooking booking = TravelBookingMapper.toEntity(dto, request, itinerary, user);

        return TravelBookingMapper.toDTO(bookingRepository.save(booking));
    }


    public List<TravelBookingResponseDTO> getByRequest(Long requestId) {

        return bookingRepository.findByTravelRequestId(requestId)
                .stream()
                .map(TravelBookingMapper::toDTO)
                .collect(Collectors.toList());
    }
}
