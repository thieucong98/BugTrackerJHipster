import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RdFPathTableFormService } from './rd-f-path-table-form.service';
import { RdFPathTableService } from '../service/rd-f-path-table.service';
import { IRdFPathTable } from '../rd-f-path-table.model';

import { RdFPathTableUpdateComponent } from './rd-f-path-table-update.component';

describe('RdFPathTable Management Update Component', () => {
  let comp: RdFPathTableUpdateComponent;
  let fixture: ComponentFixture<RdFPathTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rdFPathTableFormService: RdFPathTableFormService;
  let rdFPathTableService: RdFPathTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RdFPathTableUpdateComponent],
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
      .overrideTemplate(RdFPathTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RdFPathTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rdFPathTableFormService = TestBed.inject(RdFPathTableFormService);
    rdFPathTableService = TestBed.inject(RdFPathTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const rdFPathTable: IRdFPathTable = { id: 456 };

      activatedRoute.data = of({ rdFPathTable });
      comp.ngOnInit();

      expect(comp.rdFPathTable).toEqual(rdFPathTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRdFPathTable>>();
      const rdFPathTable = { id: 123 };
      jest.spyOn(rdFPathTableFormService, 'getRdFPathTable').mockReturnValue(rdFPathTable);
      jest.spyOn(rdFPathTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rdFPathTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rdFPathTable }));
      saveSubject.complete();

      // THEN
      expect(rdFPathTableFormService.getRdFPathTable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rdFPathTableService.update).toHaveBeenCalledWith(expect.objectContaining(rdFPathTable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRdFPathTable>>();
      const rdFPathTable = { id: 123 };
      jest.spyOn(rdFPathTableFormService, 'getRdFPathTable').mockReturnValue({ id: null });
      jest.spyOn(rdFPathTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rdFPathTable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rdFPathTable }));
      saveSubject.complete();

      // THEN
      expect(rdFPathTableFormService.getRdFPathTable).toHaveBeenCalled();
      expect(rdFPathTableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRdFPathTable>>();
      const rdFPathTable = { id: 123 };
      jest.spyOn(rdFPathTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rdFPathTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rdFPathTableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
