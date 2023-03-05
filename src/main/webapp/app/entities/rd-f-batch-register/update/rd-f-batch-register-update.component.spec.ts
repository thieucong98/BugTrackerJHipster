import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RdFBatchRegisterFormService } from './rd-f-batch-register-form.service';
import { RdFBatchRegisterService } from '../service/rd-f-batch-register.service';
import { IRdFBatchRegister } from '../rd-f-batch-register.model';

import { RdFBatchRegisterUpdateComponent } from './rd-f-batch-register-update.component';

describe('RdFBatchRegister Management Update Component', () => {
  let comp: RdFBatchRegisterUpdateComponent;
  let fixture: ComponentFixture<RdFBatchRegisterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rdFBatchRegisterFormService: RdFBatchRegisterFormService;
  let rdFBatchRegisterService: RdFBatchRegisterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RdFBatchRegisterUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RdFBatchRegisterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RdFBatchRegisterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rdFBatchRegisterFormService = TestBed.inject(RdFBatchRegisterFormService);
    rdFBatchRegisterService = TestBed.inject(RdFBatchRegisterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const rdFBatchRegister: IRdFBatchRegister = { id: 456 };

      activatedRoute.data = of({ rdFBatchRegister });
      comp.ngOnInit();

      expect(comp.rdFBatchRegister).toEqual(rdFBatchRegister);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRdFBatchRegister>>();
      const rdFBatchRegister = { id: 123 };
      jest.spyOn(rdFBatchRegisterFormService, 'getRdFBatchRegister').mockReturnValue(rdFBatchRegister);
      jest.spyOn(rdFBatchRegisterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rdFBatchRegister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rdFBatchRegister }));
      saveSubject.complete();

      // THEN
      expect(rdFBatchRegisterFormService.getRdFBatchRegister).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rdFBatchRegisterService.update).toHaveBeenCalledWith(expect.objectContaining(rdFBatchRegister));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRdFBatchRegister>>();
      const rdFBatchRegister = { id: 123 };
      jest.spyOn(rdFBatchRegisterFormService, 'getRdFBatchRegister').mockReturnValue({ id: null });
      jest.spyOn(rdFBatchRegisterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rdFBatchRegister: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rdFBatchRegister }));
      saveSubject.complete();

      // THEN
      expect(rdFBatchRegisterFormService.getRdFBatchRegister).toHaveBeenCalled();
      expect(rdFBatchRegisterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRdFBatchRegister>>();
      const rdFBatchRegister = { id: 123 };
      jest.spyOn(rdFBatchRegisterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rdFBatchRegister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rdFBatchRegisterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
