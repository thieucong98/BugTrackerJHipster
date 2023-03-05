import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRdFBatchRegister } from '../rd-f-batch-register.model';
import { RdFBatchRegisterService } from '../service/rd-f-batch-register.service';

import { RdFBatchRegisterRoutingResolveService } from './rd-f-batch-register-routing-resolve.service';

describe('RdFBatchRegister routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RdFBatchRegisterRoutingResolveService;
  let service: RdFBatchRegisterService;
  let resultRdFBatchRegister: IRdFBatchRegister | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(RdFBatchRegisterRoutingResolveService);
    service = TestBed.inject(RdFBatchRegisterService);
    resultRdFBatchRegister = undefined;
  });

  describe('resolve', () => {
    it('should return IRdFBatchRegister returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRdFBatchRegister = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRdFBatchRegister).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRdFBatchRegister = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRdFBatchRegister).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IRdFBatchRegister>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRdFBatchRegister = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRdFBatchRegister).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
